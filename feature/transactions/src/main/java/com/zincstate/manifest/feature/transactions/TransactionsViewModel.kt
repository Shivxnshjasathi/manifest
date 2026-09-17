package com.zincstate.manifest.feature.transactions

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.common.DateUtils
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import com.zincstate.manifest.core.database.entity.AccountEntity
import com.zincstate.manifest.core.database.entity.CategoryEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity
import com.zincstate.manifest.core.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TransactionUiItem(
    val id: String,
    val categoryIcon: String,
    val categoryName: String,
    val note: String,
    val accountName: String,
    val amount: String,
    val isIncome: Boolean,
    val date: String,
    val type: String
)

@Immutable
data class CategoryTotalUiItem(
    val categoryName: String,
    val categoryIcon: String,
    val totalAmount: String,
    val rawAmount: Double,
    val isIncome: Boolean,
    val percentage: Float = 0f
)

@Stable
data class TransactionsUiState(
    val currentMonth: String = DateUtils.currentYearMonth(),
    val monthDisplayText: String = DateUtils.formatMonthYear(DateUtils.currentYearMonth()),
    val isAmountVisible: Boolean = true,
    val totalIncome: String = "₹0",
    val totalExpense: String = "₹0",
    val total: String = "₹0",
    val groupedTransactions: Map<String, List<TransactionUiItem>> = emptyMap(),
    val dailyTotals: Map<String, String> = emptyMap(),
    val dailyTotalsRaw: Map<String, Double> = emptyMap(),
    val isLoading: Boolean = true,
    val selectedTab: Int = 0, // 0=Daily, 1=Calendar, 2=Monthly, 3=Summary, 4=Description
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<TransactionUiItem> = emptyList(),
    val transactionDatesInMonth: Set<Int> = emptySet(),
    val selectedTransactionIds: Set<String> = emptySet(),
    val isSelectionMode: Boolean = false,
    val selectedCalendarDate: String? = DateUtils.today(),
    val monthlyCategoryTotals: List<CategoryTotalUiItem> = emptyList(),
    val descriptionGroupedTransactions: Map<String, List<TransactionUiItem>> = emptyMap(),
    val filterType: String? = null,
    val filterAccountId: String? = null,
    val filterCategoryId: String? = null
)

private data class TransactionsDbData(
    val transactions: List<TransactionEntity>,
    val categories: List<CategoryEntity>,
    val accounts: List<AccountEntity>,
    val splits: List<TransactionSplitEntity>
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao,
    private val splitDao: TransactionSplitDao,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(DateUtils.currentYearMonth())
    private val _selectedTab = MutableStateFlow(0)
    private val _isSearchActive = MutableStateFlow(false)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedTransactionIds = MutableStateFlow<Set<String>>(emptySet())
    private val _selectedCalendarDate = MutableStateFlow<String?>(DateUtils.today())
    private val _filterType = MutableStateFlow<String?>(null)
    private val _filterAccountId = MutableStateFlow<String?>(null)
    private val _filterCategoryId = MutableStateFlow<String?>(null)

    val uiState: StateFlow<TransactionsUiState> = combine(
        _currentMonth, _selectedTab, _isSearchActive, _searchQuery,
        _selectedTransactionIds, _selectedCalendarDate, _filterType,
        _filterAccountId, _filterCategoryId, preferencesDataStore.isAmountVisible
    ) { args ->
        val month = args[0] as String
        @Suppress("UNCHECKED_CAST")
        val selectedIds = args[4] as Set<String>
        TransactionsUiState(
            currentMonth = month,
            monthDisplayText = DateUtils.formatMonthYear(month),
            selectedTab = args[1] as Int,
            isSearchActive = args[2] as Boolean,
            searchQuery = args[3] as String,
            selectedTransactionIds = selectedIds,
            isSelectionMode = selectedIds.isNotEmpty(),
            selectedCalendarDate = args[5] as String?,
            filterType = args[6] as String?,
            filterAccountId = args[7] as String?,
            filterCategoryId = args[8] as String?,
            isAmountVisible = args[9] as Boolean
        )
    }.combine(
        _currentMonth.flatMapLatest { month ->
            combine(
                transactionDao.getTransactionsByMonth(month),
                categoryDao.getAllCategories(),
                accountDao.getAllAccounts()
            ) { txs, cats, accs ->
                txs to (cats to accs)
            }.flatMapLatest { (txs, dbData) ->
                val txIds = txs.map { it.id }
                if (txIds.isEmpty()) {
                    flowOf(TransactionsDbData(txs, dbData.first, dbData.second, emptyList()))
                } else {
                    splitDao.getSplitsForTransactions(txIds).map { splits ->
                        TransactionsDbData(txs, dbData.first, dbData.second, splits)
                    }
                }
            }
        }
    ) { state, dbData ->
        val txs = dbData.transactions
        val categoryMap = dbData.categories.associateBy { it.id }
        val accountMap = dbData.accounts.associateBy { it.id }
        val splitMap = dbData.splits.groupBy { it.transactionId }

        val filteredTxs = txs.filter { tx ->
            (state.filterType == null || tx.type == state.filterType) &&
            (state.filterAccountId == null || tx.accountId == state.filterAccountId) &&
            (state.filterCategoryId == null || tx.categoryId == state.filterCategoryId)
        }

        val uiItems = filteredTxs.map { mapToUiItem(it, categoryMap, accountMap) }
        val grouped = uiItems.groupBy { it.date }
        
        // Calculate daily totals (net share)
        val dailyTotalsRaw = grouped.mapValues { (_, items) ->
            items.sumOf { item ->
                val raw = item.amount.replace("₹", "").replace(",", "").replace("-", "").replace("+", "").toDoubleOrNull() ?: 0.0
                when (item.type) {
                    "INCOME" -> raw
                    "EXPENSE" -> {
                        val txSplits = splitMap[item.id] ?: emptyList()
                        -(raw - txSplits.sumOf { it.amount })
                    }
                    else -> 0.0
                }
            }
        }

        // Aggregate Month Totals
        val totalIncome = filteredTxs.filter { it.type == "INCOME" }.sumOf { it.amount }
        val totalExpense = filteredTxs.filter { it.type == "EXPENSE" }.sumOf { tx ->
            val splitAmount = splitMap[tx.id]?.sumOf { it.amount } ?: 0.0
            tx.amount - splitAmount
        }

        // Monthly Category Breakdown
        val categoryTotals = uiItems.groupBy { it.categoryName }.map { (catName, items) ->
            val totalRaw = items.sumOf { item ->
                val raw = item.amount.replace("₹", "").replace(",", "").replace("-", "").replace("+", "").toDoubleOrNull() ?: 0.0
                if (item.isIncome) raw else {
                    val txSplits = splitMap[item.id] ?: emptyList()
                    -(raw - txSplits.sumOf { it.amount })
                }
            }
            CategoryTotalUiItem(
                categoryName = catName,
                categoryIcon = items.first().categoryIcon,
                totalAmount = CurrencyFormatter.formatWithSign(Math.abs(totalRaw), totalRaw >= 0),
                rawAmount = totalRaw,
                isIncome = totalRaw >= 0
            )
        }.sortedByDescending { Math.abs(it.rawAmount) }

        val absTotalExpense = categoryTotals.filter { !it.isIncome }.sumOf { Math.abs(it.rawAmount) }
        val finalCategoryTotals = categoryTotals.map {
            it.copy(percentage = if (absTotalExpense > 0 && !it.isIncome) (Math.abs(it.rawAmount) / absTotalExpense).toFloat() else 0f)
        }

        state.copy(
            groupedTransactions = grouped,
            dailyTotals = dailyTotalsRaw.mapValues { CurrencyFormatter.format(it.value) },
            dailyTotalsRaw = dailyTotalsRaw,
            totalIncome = CurrencyFormatter.format(totalIncome),
            totalExpense = CurrencyFormatter.format(totalExpense),
            total = CurrencyFormatter.format(totalIncome - totalExpense),
            isLoading = false,
            transactionDatesInMonth = filteredTxs.map { DateUtils.getDayOfMonth(it.date) }.toSet(),
            monthlyCategoryTotals = finalCategoryTotals,
            descriptionGroupedTransactions = uiItems.groupBy { it.note.ifBlank { "No Description" } }
        )
    }.combine(
        combine(_isSearchActive, _searchQuery) { active, query -> active to query }
            .flatMapLatest { (active, query) ->
                if (active && query.isNotBlank()) {
                    combine(
                        transactionDao.searchTransactions(query),
                        categoryDao.getAllCategories(),
                        accountDao.getAllAccounts()
                    ) { txs, cats, accs ->
                        val catMap = cats.associateBy { it.id }
                        val accMap = accs.associateBy { it.id }
                        txs.map { mapToUiItem(it, catMap, accMap) }
                    }
                } else flowOf(emptyList())
            }
    ) { state, searchResults ->
        state.copy(searchResults = searchResults)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransactionsUiState()
    )

    fun navigateMonth(delta: Int) {
        _currentMonth.value = if (delta > 0) DateUtils.nextMonth(_currentMonth.value) else DateUtils.previousMonth(_currentMonth.value)
    }

    fun selectTab(index: Int) { _selectedTab.value = index }
    fun selectCalendarDate(date: String?) { _selectedCalendarDate.value = date }
    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) _searchQuery.value = ""
    }
    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun toggleAmountVisibility() {
        viewModelScope.launch { preferencesDataStore.setAmountVisible(!uiState.value.isAmountVisible) }
    }

    fun toggleSelection(id: String) {
        val current = _selectedTransactionIds.value.toMutableSet()
        if (current.contains(id)) current.remove(id) else current.add(id)
        _selectedTransactionIds.value = current
    }

    fun clearSelection() { _selectedTransactionIds.value = emptySet() }

    fun deleteSelectedTransactions() {
        val ids = _selectedTransactionIds.value.toList()
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                ids.forEach { id ->
                    transactionDao.getTransactionById(id)?.let { tx -> reverseTransactionImpact(tx) }
                }
                transactionDao.deleteByIds(ids)
                _selectedTransactionIds.value = emptySet()
            }
        }
    }

    fun deleteTransactions(ids: List<String>) {
        viewModelScope.launch {
            ids.forEach { id ->
                transactionDao.getTransactionById(id)?.let { tx -> reverseTransactionImpact(tx) }
            }
            transactionDao.deleteByIds(ids)
        }
    }

    private suspend fun reverseTransactionImpact(tx: TransactionEntity) {
        when (tx.type) {
            "INCOME" -> accountDao.updateBalance(tx.accountId, -tx.amount)
            "EXPENSE" -> accountDao.updateBalance(tx.accountId, tx.amount)
            "TRANSFER" -> {
                accountDao.updateBalance(tx.accountId, tx.amount)
                tx.toAccountId?.let { accountDao.updateBalance(it, -tx.amount) }
            }
        }
    }

    fun setFilter(type: String?, accountId: String?, categoryId: String?) {
        _filterType.value = type
        _filterAccountId.value = accountId
        _filterCategoryId.value = categoryId
    }

    fun clearFilters() {
        _filterType.value = null
        _filterAccountId.value = null
        _filterCategoryId.value = null
    }

    private fun mapToUiItem(
        tx: TransactionEntity,
        categoryMap: Map<String, CategoryEntity>,
        accountMap: Map<String, AccountEntity>
    ): TransactionUiItem {
        val category = categoryMap[tx.categoryId]
        val account = accountMap[tx.accountId]
        val isIncome = tx.type == "INCOME"
        return TransactionUiItem(
            id = tx.id,
            categoryIcon = category?.icon ?: "📝",
            categoryName = category?.name ?: "Unknown",
            note = tx.note,
            accountName = account?.name ?: "Unknown",
            amount = CurrencyFormatter.formatWithSign(tx.amount, isIncome),
            isIncome = isIncome,
            date = tx.date,
            type = tx.type
        )
    }
}

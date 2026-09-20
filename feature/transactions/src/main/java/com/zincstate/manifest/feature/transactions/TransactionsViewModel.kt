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

import java.time.LocalDate

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
    val type: String,
    val hasAttachment: Boolean = false
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

@Immutable
data class WeeklySummary(
    val weekNumber: Int,
    val dateRange: String,
    val income: Double,
    val expense: Double,
    val net: Double,
    val transactions: List<TransactionUiItem>
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
    val dailyIncomeRaw: Map<String, Double> = emptyMap(),
    val dailyExpenseRaw: Map<String, Double> = emptyMap(),
    val weeklySummaries: List<WeeklySummary> = emptyList(),
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
    val filterAccountIds: Set<String> = emptySet(),
    val filterCategoryIds: Set<String> = emptySet(),
    val filterMinAmount: Double? = null,
    val filterMaxAmount: Double? = null,
    val filterDateRange: Pair<LocalDate?, LocalDate?>? = null,
    val accounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val currencySymbol: String = "₹"
)

private data class TransactionsDbData(
    val transactions: List<TransactionEntity>,
    val categories: List<CategoryEntity>,
    val accounts: List<AccountEntity>,
    val splits: List<TransactionSplitEntity>,
    val currencySymbol: String
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
    private val _filterAccountIds = MutableStateFlow<Set<String>>(emptySet())
    private val _filterCategoryIds = MutableStateFlow<Set<String>>(emptySet())
    private val _filterMinAmount = MutableStateFlow<Double?>(null)
    private val _filterMaxAmount = MutableStateFlow<Double?>(null)
    private val _filterDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>?>(null)

    val uiState: StateFlow<TransactionsUiState> = combine(
        _currentMonth, _selectedTab, _isSearchActive, _searchQuery,
        _selectedTransactionIds, _selectedCalendarDate, _filterType,
        _filterAccountIds, _filterCategoryIds, _filterMinAmount,
        _filterMaxAmount, _filterDateRange, preferencesDataStore.isAmountVisible,
        categoryDao.getAllCategories(), accountDao.getAllAccounts(),
        preferencesDataStore.currencySymbol
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        TransactionsUiState(
            currentMonth = args[0] as String,
            monthDisplayText = DateUtils.formatMonthYear(args[0] as String),
            selectedTab = args[1] as Int,
            isSearchActive = args[2] as Boolean,
            searchQuery = args[3] as String,
            selectedTransactionIds = args[4] as Set<String>,
            isSelectionMode = (args[4] as Set<String>).isNotEmpty(),
            selectedCalendarDate = args[5] as String?,
            filterType = args[6] as String?,
            filterAccountIds = args[7] as Set<String>,
            filterCategoryIds = args[8] as Set<String>,
            filterMinAmount = args[9] as Double?,
            filterMaxAmount = args[10] as Double?,
            filterDateRange = args[11] as Pair<LocalDate?, LocalDate?>?,
            isAmountVisible = args[12] as Boolean,
            categories = args[13] as List<CategoryEntity>,
            accounts = args[14] as List<AccountEntity>,
            currencySymbol = args[15] as String
        )
    }.combine(
        _currentMonth.flatMapLatest { month ->
            combine(
                transactionDao.getTransactionsByMonth(month),
                categoryDao.getAllCategories(),
                accountDao.getAllAccounts(),
                preferencesDataStore.currencySymbol
            ) { txs, cats, accs, symbol ->
                txs to (Triple(cats, accs, symbol))
            }.flatMapLatest { (txs, dbData) ->
                val txIds = txs.map { it.id }
                if (txIds.isEmpty()) {
                    flowOf(TransactionsDbData(txs, dbData.first, dbData.second, emptyList(), dbData.third))
                } else {
                    splitDao.getSplitsForTransactions(txIds).map { splits ->
                        TransactionsDbData(txs, dbData.first, dbData.second, splits, dbData.third)
                    }
                }
            }
        }
    ) { state, dbData ->
        val txs = dbData.transactions
        val categoryMap = dbData.categories.associateBy { it.id }
        val accountMap = dbData.accounts.associateBy { it.id }
        val splitMap = dbData.splits.groupBy { it.transactionId }
        val currencySymbol = dbData.currencySymbol

        val filteredTxs = txs.filter { tx ->
            (state.filterType == null || tx.type == state.filterType) &&
            (state.filterAccountIds.isEmpty() || state.filterAccountIds.contains(tx.accountId)) &&
            (state.filterCategoryIds.isEmpty() || state.filterCategoryIds.contains(tx.categoryId)) &&
            (state.filterMinAmount == null || tx.amount >= state.filterMinAmount) &&
            (state.filterMaxAmount == null || tx.amount <= state.filterMaxAmount) &&
            (state.filterDateRange == null || (
                (state.filterDateRange.first == null || !LocalDate.parse(tx.date).isBefore(state.filterDateRange.first)) &&
                (state.filterDateRange.second == null || !LocalDate.parse(tx.date).isAfter(state.filterDateRange.second))
            ))
        }

        val uiItems = filteredTxs.map { mapToUiItem(it, categoryMap, accountMap, currencySymbol) }
        val grouped = uiItems.groupBy { it.date }
        
        // Calculate daily totals (net share)
        val dailyIncomeRaw = grouped.mapValues { (_, items) ->
            items.filter { it.type == "INCOME" }.sumOf { item ->
                item.amount.replace(currencySymbol, "").replace(",", "").replace("+", "").replace("-", "").toDoubleOrNull() ?: 0.0
            }
        }
        val dailyExpenseRaw = grouped.mapValues { (_, items) ->
            items.filter { it.type == "EXPENSE" }.sumOf { item ->
                val raw = item.amount.replace(currencySymbol, "").replace(",", "").replace("-", "").replace("+", "").toDoubleOrNull() ?: 0.0
                val txSplits = splitMap[item.id] ?: emptyList()
                raw - txSplits.sumOf { it.amount }
            }
        }
        val dailyTotalsRaw = grouped.mapValues { (date, _) ->
            (dailyIncomeRaw[date] ?: 0.0) - (dailyExpenseRaw[date] ?: 0.0)
        }

        // Aggregate Month Totals
        val totalIncome = filteredTxs.filter { it.type == "INCOME" }.sumOf { it.amount }
        val totalExpense = filteredTxs.filter { it.type == "EXPENSE" }.sumOf { tx ->
            val splitAmount = splitMap[tx.id]?.sumOf { it.amount } ?: 0.0
            tx.amount - splitAmount
        }

        // Weekly Summaries
        val weeklySummaries = mutableListOf<WeeklySummary>()
        val monthYear = try { LocalDate.parse("${state.currentMonth}-01") } catch(e: Exception) { LocalDate.now().withDayOfMonth(1) }
        val daysInMonth = monthYear.lengthOfMonth()
        
        for (week in 0 until 5) {
            val startDay = week * 7 + 1
            if (startDay > daysInMonth) break
            val endDay = minOf((week + 1) * 7, daysInMonth)
            
            val weekTxs = uiItems.filter { item ->
                val day = DateUtils.getDayOfMonth(item.date)
                day in startDay..endDay
            }
            
            val income = weekTxs.filter { it.type == "INCOME" }.sumOf { 
                it.amount.replace(currencySymbol, "").replace(",", "").replace("+", "").replace("-", "").toDoubleOrNull() ?: 0.0 
            }
            val expense = weekTxs.filter { it.type == "EXPENSE" }.sumOf { 
                val raw = it.amount.replace(currencySymbol, "").replace(",", "").replace("-", "").replace("+", "").toDoubleOrNull() ?: 0.0
                val txSplits = splitMap[it.id] ?: emptyList()
                raw - txSplits.sumOf { s -> s.amount }
            }
            
            weeklySummaries.add(
                WeeklySummary(
                    weekNumber = week + 1,
                    dateRange = "${monthYear.month.name.take(3)} $startDay - $endDay",
                    income = income,
                    expense = expense,
                    net = income - expense,
                    transactions = weekTxs
                )
            )
        }

        // Monthly Category Breakdown
        val categoryTotals = uiItems.groupBy { it.categoryName }.map { (catName, items) ->
            val totalRaw = items.sumOf { item ->
                val raw = item.amount.replace(currencySymbol, "").replace(",", "").replace("-", "").replace("+", "").toDoubleOrNull() ?: 0.0
                if (item.isIncome) raw else {
                    val txSplits = splitMap[item.id] ?: emptyList()
                    -(raw - txSplits.sumOf { it.amount })
                }
            }
            CategoryTotalUiItem(
                categoryName = catName,
                categoryIcon = items.first().categoryIcon,
                totalAmount = CurrencyFormatter.formatWithSign(Math.abs(totalRaw), totalRaw >= 0, currencySymbol),
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
            dailyTotals = dailyTotalsRaw.mapValues { CurrencyFormatter.format(it.value, currencySymbol = currencySymbol) },
            dailyTotalsRaw = dailyTotalsRaw,
            dailyIncomeRaw = dailyIncomeRaw,
            dailyExpenseRaw = dailyExpenseRaw,
            totalIncome = CurrencyFormatter.format(totalIncome, currencySymbol = currencySymbol),
            totalExpense = CurrencyFormatter.format(totalExpense, currencySymbol = currencySymbol),
            total = CurrencyFormatter.format(totalIncome - totalExpense, currencySymbol = currencySymbol),
            weeklySummaries = weeklySummaries,
            isLoading = false,
            transactionDatesInMonth = filteredTxs.map { DateUtils.getDayOfMonth(it.date) }.toSet(),
            monthlyCategoryTotals = finalCategoryTotals,
            descriptionGroupedTransactions = uiItems.groupBy { it.note.ifBlank { "No Description" } }
        )
    }.combine(
        combine(_isSearchActive, _searchQuery, preferencesDataStore.currencySymbol) { active, query, symbol -> Triple(active, query, symbol) }
            .flatMapLatest { (active, query, symbol) ->
                if (active && query.isNotBlank()) {
                    combine(
                        transactionDao.searchTransactions(query),
                        categoryDao.getAllCategories(),
                        accountDao.getAllAccounts()
                    ) { txs, cats, accs ->
                        val catMap = cats.associateBy { it.id }
                        val accMap = accs.associateBy { it.id }
                        txs.map { mapToUiItem(it, catMap, accMap, symbol) }
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

    fun setFilter(
        type: String?,
        accountIds: Set<String>,
        categoryIds: Set<String>,
        minAmount: Double? = null,
        maxAmount: Double? = null,
        dateRange: Pair<LocalDate?, LocalDate?>? = null
    ) {
        _filterType.value = type
        _filterAccountIds.value = accountIds
        _filterCategoryIds.value = categoryIds
        _filterMinAmount.value = minAmount
        _filterMaxAmount.value = maxAmount
        _filterDateRange.value = dateRange
    }

    fun clearFilters() {
        _filterType.value = null
        _filterAccountIds.value = emptySet()
        _filterCategoryIds.value = emptySet()
        _filterMinAmount.value = null
        _filterMaxAmount.value = null
        _filterDateRange.value = null
    }

    private fun mapToUiItem(
        tx: TransactionEntity,
        categoryMap: Map<String, CategoryEntity>,
        accountMap: Map<String, AccountEntity>,
        currencySymbol: String
    ): TransactionUiItem {
        val category = tx.categoryId?.let { categoryMap[it] }
        val account = accountMap[tx.accountId]
        val isIncome = tx.type == "INCOME"
        val isTransfer = tx.type == "TRANSFER"
        
        return TransactionUiItem(
            id = tx.id,
            categoryIcon = if (isTransfer) "↔️" else category?.icon ?: "📝",
            categoryName = if (isTransfer) "Transfer" else category?.name ?: "Unknown",
            note = tx.note,
            accountName = account?.name ?: "Unknown",
            amount = if (isTransfer) CurrencyFormatter.format(tx.amount, currencySymbol = currencySymbol) else CurrencyFormatter.formatWithSign(tx.amount, isIncome, currencySymbol),
            isIncome = isIncome,
            date = tx.date,
            type = tx.type,
            hasAttachment = !tx.attachmentPath.isNullOrBlank()
        )
    }
}

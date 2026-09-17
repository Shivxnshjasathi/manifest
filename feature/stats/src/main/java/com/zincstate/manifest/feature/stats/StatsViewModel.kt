package com.zincstate.manifest.feature.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.DailyTotal
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.entity.CategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CategoryStatsUiItem(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val totalAmount: Double,
    val formattedAmount: String,
    val percentage: Float
)

data class StatsUiState(
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val selectedType: String = "EXPENSE", // "EXPENSE" or "INCOME"
    val totalAmount: String = "₹0",
    val categoryBreakdown: List<CategoryStatsUiItem> = emptyList(),
    val last30DaysExpenses: List<DailyTotal> = emptyList(),
    val totalIncomeForMonth: String = "₹0",
    val totalExpenseForMonth: String = "₹0",
    val isLoading: Boolean = true
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    private val _selectedType = MutableStateFlow("EXPENSE")

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

    val uiState: StateFlow<StatsUiState> = combine(
        _currentMonth,
        _selectedType,
        _currentMonth.flatMapLatest { date -> transactionDao.getCategoryBreakdown(date.format(formatter), "EXPENSE") },
        _currentMonth.flatMapLatest { date -> transactionDao.getCategoryBreakdown(date.format(formatter), "INCOME") },
        categoryDao.getAllCategories(),
        transactionDao.getDailyExpensesSince(LocalDate.now().minusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE))
    ) { args ->
        val currentMonth = args[0] as LocalDate
        val selectedType = args[1] as String
        @Suppress("UNCHECKED_CAST")
        val expenseBreakdown = args[2] as List<com.zincstate.manifest.core.database.dao.CategoryTotal>
        @Suppress("UNCHECKED_CAST")
        val incomeBreakdown = args[3] as List<com.zincstate.manifest.core.database.dao.CategoryTotal>
        @Suppress("UNCHECKED_CAST")
        val categories = args[4] as List<CategoryEntity>
        @Suppress("UNCHECKED_CAST")
        val last30Days = args[5] as List<DailyTotal>
        
        val categoryMap = categories.associateBy { it.id }
        
        val activeBreakdown = if (selectedType == "EXPENSE") expenseBreakdown else incomeBreakdown
        val totalAmount = activeBreakdown.sumOf { it.total }
        
        val totalIncome = incomeBreakdown.sumOf { it.total }
        val totalExpense = expenseBreakdown.sumOf { it.total }

        val categoryUiItems = activeBreakdown.map { catTotal ->
            val category = categoryMap[catTotal.categoryId]
            CategoryStatsUiItem(
                categoryId = catTotal.categoryId,
                categoryName = category?.name ?: "Unknown",
                categoryIcon = category?.icon ?: "📝",
                totalAmount = catTotal.total,
                formattedAmount = CurrencyFormatter.format(catTotal.total),
                percentage = if (totalAmount > 0) (catTotal.total / totalAmount).toFloat() else 0f
            )
        }.sortedByDescending { it.totalAmount }

        StatsUiState(
            currentMonth = currentMonth,
            selectedType = selectedType,
            totalAmount = CurrencyFormatter.format(totalAmount),
            categoryBreakdown = categoryUiItems,
            last30DaysExpenses = last30Days,
            totalIncomeForMonth = CurrencyFormatter.format(totalIncome),
            totalExpenseForMonth = CurrencyFormatter.format(totalExpense),
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())

    fun toggleType(type: String) {
        _selectedType.value = type
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }
}

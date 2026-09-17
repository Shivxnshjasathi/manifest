package com.zincstate.manifest.feature.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.BudgetDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import com.zincstate.manifest.core.database.entity.BudgetEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

data class BudgetUiItem(
    val id: String,
    val categoryId: String,
    val limit: Double,
    val spent: Double,
    val yearMonth: String
)

private data class BudgetDbData(
    val budgets: List<BudgetEntity>,
    val transactions: List<TransactionEntity>,
    val splits: List<TransactionSplitEntity>
)

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao,
    private val splitDao: TransactionSplitDao
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(LocalDate.now().withDayOfMonth(1))
    val currentMonth = _currentMonth.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

    val budgets: StateFlow<List<BudgetUiItem>> = _currentMonth.flatMapLatest { date ->
        val yearMonth = date.format(formatter)
        combine(
            budgetDao.getBudgetsByMonth(yearMonth),
            transactionDao.getTransactionsByMonth(yearMonth),
            splitDao.getSplitsForTransactionsByMonth(yearMonth)
        ) { budgets, txs, splits ->
            BudgetDbData(budgets, txs, splits)
        }.map { data ->
            val splitMap = data.splits.groupBy { it.transactionId }
            
            data.budgets.map { entity ->
                val spent = if (entity.categoryId == "overall") {
                    data.transactions.filter { it.type == "EXPENSE" }.sumOf { tx ->
                        val splitAmount = splitMap[tx.id]?.sumOf { it.amount } ?: 0.0
                        tx.amount - splitAmount
                    }
                } else {
                    data.transactions.filter { it.type == "EXPENSE" && it.categoryId == entity.categoryId }.sumOf { tx ->
                        val splitAmount = splitMap[tx.id]?.sumOf { it.amount } ?: 0.0
                        tx.amount - splitAmount
                    }
                }
                BudgetUiItem(
                    id = entity.id,
                    categoryId = entity.categoryId,
                    limit = entity.amount,
                    spent = spent,
                    yearMonth = entity.yearMonth
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun addBudget(categoryId: String, amount: Double) {
        viewModelScope.launch {
            val yearMonth = _currentMonth.value.format(formatter)
            // check if exists
            val existing = budgetDao.getBudget(categoryId, yearMonth)
            if (existing != null) {
                budgetDao.insert(existing.copy(amount = amount))
            } else {
                budgetDao.insert(
                    BudgetEntity(
                        id = UUID.randomUUID().toString(),
                        categoryId = categoryId,
                        amount = amount,
                        yearMonth = yearMonth
                    )
                )
            }
        }
    }

    fun deleteBudget(id: String) {
        viewModelScope.launch {
            budgetDao.deleteById(id)
        }
    }
}

package com.zincstate.manifest.feature.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.GoalDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.entity.GoalEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalDao: GoalDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : ViewModel() {

    val goals: StateFlow<List<GoalEntity>> = goalDao.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGoal(name: String, targetAmount: Double, color: String) {
        viewModelScope.launch {
            goalDao.insert(
                GoalEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    targetAmount = targetAmount,
                    currentAmount = 0.0,
                    color = color
                )
            )
        }
    }

    fun addFundsToGoal(goalId: String, amount: Double, accountId: String?) {
        viewModelScope.launch {
            val existing = goalDao.getGoalById(goalId)
            if (existing != null) {
                // Update goal amount
                goalDao.update(existing.copy(currentAmount = existing.currentAmount + amount))
                
                // If account provided, create a transaction and reduce balance
                if (accountId != null) {
                    val tx = TransactionEntity(
                        id = UUID.randomUUID().toString(),
                        type = "EXPENSE",
                        amount = amount,
                        date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        categoryId = "savings_goal", // Special internal category or use "Other"
                        accountId = accountId,
                        note = "Funds added to goal: ${existing.name}",
                        createdAt = System.currentTimeMillis()
                    )
                    transactionDao.insert(tx)
                    accountDao.updateBalance(accountId, -amount)
                }
            }
        }
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch {
            goalDao.delete(goal)
        }
    }
}

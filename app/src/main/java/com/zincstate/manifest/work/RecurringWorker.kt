package com.zincstate.manifest.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.entity.TransactionEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@HiltWorker
class RecurringWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val recurringDao: RecurringTransactionDao,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        
        try {
            val dueItems = recurringDao.getDueRecurring(todayStr)
            if (dueItems.isEmpty()) return Result.success()

            for (recurring in dueItems) {
                var currentRunDate = LocalDate.parse(recurring.nextRunDate, DateTimeFormatter.ISO_LOCAL_DATE)
                val today = LocalDate.now()
                
                var iterations = 0
                while (!currentRunDate.isAfter(today) && iterations < 365) {
                    val tx = TransactionEntity(
                        id = UUID.randomUUID().toString(),
                        type = recurring.type,
                        amount = recurring.amount,
                        categoryId = recurring.categoryId,
                        accountId = recurring.accountId,
                        toAccountId = recurring.toAccountId,
                        note = recurring.note,
                        description = recurring.description,
                        superCategory = recurring.superCategory,
                        date = currentRunDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                        createdAt = System.currentTimeMillis(),
                        recurringId = recurring.id
                    )
                    
                    transactionDao.insert(tx)
                    applyTransactionImpact(tx)
                    
                    currentRunDate = when (recurring.frequency) {
                        "DAILY" -> currentRunDate.plusDays(1)
                        "WEEKLY" -> currentRunDate.plusWeeks(1)
                        "MONTHLY" -> currentRunDate.plusMonths(1)
                        "YEARLY" -> currentRunDate.plusYears(1)
                        else -> currentRunDate.plusMonths(1)
                    }
                    iterations++
                }

                recurringDao.update(
                    recurring.copy(nextRunDate = currentRunDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                )
            }
            
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

    private suspend fun applyTransactionImpact(tx: TransactionEntity) {
        when (tx.type) {
            "INCOME" -> accountDao.updateBalance(tx.accountId, tx.amount)
            "EXPENSE" -> accountDao.updateBalance(tx.accountId, -tx.amount)
            "TRANSFER" -> {
                accountDao.updateBalance(tx.accountId, -tx.amount)
                tx.toAccountId?.let { accountDao.updateBalance(it, tx.amount) }
            }
        }
    }
}

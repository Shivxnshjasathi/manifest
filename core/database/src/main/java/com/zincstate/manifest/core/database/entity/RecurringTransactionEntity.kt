package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey val id: String,
    val type: String, // INCOME, EXPENSE, TRANSFER
    val amount: Double,
    val categoryId: String,
    val accountId: String,
    val toAccountId: String? = null,
    val note: String = "",
    val description: String = "",
    val superCategory: String? = null,
    val frequency: String, // DAILY, WEEKLY, MONTHLY, YEARLY
    val startDate: String,
    val nextRunDate: String
)

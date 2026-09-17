package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["date"]),
        Index(value = ["accountId"]),
        Index(value = ["categoryId"]),
        Index(value = ["recurringId"])
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    val type: String, // INCOME, EXPENSE, TRANSFER
    val amount: Double,
    val date: String, // ISO 8601
    val categoryId: String,
    val accountId: String,
    val toAccountId: String? = null,
    val note: String = "",
    val description: String = "",
    val attachmentPath: String? = null,
    val superCategory: String? = null, // NEEDS, WANTS, INVESTMENT
    val isSettled: Boolean = true,
    val recurringId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

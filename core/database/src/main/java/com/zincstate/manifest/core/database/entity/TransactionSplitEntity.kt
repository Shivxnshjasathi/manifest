package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_splits",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["transactionId"]),
        Index(value = ["contactId"])
    ]
)
data class TransactionSplitEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val contactId: String,
    val amount: Double,
    val isSettled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

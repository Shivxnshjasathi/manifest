package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val date: String, // ISO 8601 date string "2026-09-17"
    val category: String, // Category ID
    val accountId: String, // Account ID
    val toAccountId: String? = null, // For transfers
    val note: String = "",
    val description: String = "",
    val attachmentPath: String? = null, // Relative path to encrypted file
    val superCategory: SuperCategory? = null,
    val isSettled: Boolean = true,
    val recurringId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

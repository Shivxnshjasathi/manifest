package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecurringTransaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val categoryId: String,
    val accountId: String,
    val toAccountId: String? = null,
    val note: String = "",
    val description: String = "",
    val superCategory: SuperCategory? = null,
    val frequency: Frequency,
    val startDate: String,
    val nextRunDate: String
)

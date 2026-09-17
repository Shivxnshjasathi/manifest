package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class MonthlySummary(
    val yearMonth: String, // "2026-09"
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val byCategory: Map<String, Double> = emptyMap() // categoryId → amount
)

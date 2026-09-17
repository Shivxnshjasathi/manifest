package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_summaries")
data class MonthlySummaryEntity(
    @PrimaryKey val yearMonth: String, // "2026-09"
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val byCategory: String = "{}" // JSON map: categoryId → amount
)

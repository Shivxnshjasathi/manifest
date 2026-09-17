package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val categoryId: String, // "overall" or Category.id
    val amount: Double,
    val yearMonth: String // "2026-09"
)

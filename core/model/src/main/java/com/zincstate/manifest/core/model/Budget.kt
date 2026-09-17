package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Budget(
    val id: String,
    val categoryId: String, // "overall" or Category.id
    val amount: Double,
    val yearMonth: String // "2026-09"
)

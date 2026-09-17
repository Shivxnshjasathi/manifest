package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Goal(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: String? = null,
    val color: String = "#FF5A5F" // Hex color
)

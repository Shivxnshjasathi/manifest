package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
    val id: String,
    val name: String,
    val icon: String, // Emoji string
    val type: CategoryType
)

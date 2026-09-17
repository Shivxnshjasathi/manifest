package com.zincstate.manifest.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Account(
    val id: String,
    val name: String,
    val group: AccountGroup,
    val balance: Double = 0.0,
    val settlementDate: Int = 0,
    val paymentDate: Int = 0
)

package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val group: String, // AccountGroup enum name
    val balance: Double = 0.0,
    val settlementDate: Int = 0,
    val paymentDate: Int = 0
)

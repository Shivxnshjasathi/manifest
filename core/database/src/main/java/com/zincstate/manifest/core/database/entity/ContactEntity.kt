package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

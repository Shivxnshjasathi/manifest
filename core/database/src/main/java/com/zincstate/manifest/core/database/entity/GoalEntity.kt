package com.zincstate.manifest.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: String? = null,
    val color: String = "#FF5A5F"
)

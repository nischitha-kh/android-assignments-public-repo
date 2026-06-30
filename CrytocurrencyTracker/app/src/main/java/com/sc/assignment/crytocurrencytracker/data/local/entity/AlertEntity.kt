package com.sc.assignment.crytocurrencytracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: String,
    val coinSymbol: String,
    val targetPrice: Double,
    val isAbove: Boolean,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

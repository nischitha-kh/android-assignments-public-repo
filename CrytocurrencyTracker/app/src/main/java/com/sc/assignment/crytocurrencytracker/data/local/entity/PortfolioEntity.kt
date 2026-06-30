package com.sc.assignment.crytocurrencytracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey val coinId: String,
    val count: Double,
    val averageBuyPrice: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

package com.sc.assignment.crytocurrencytracker.domain.model

data class Alert(
    val id: Int = 0,
    val coinId: String,
    val coinSymbol: String,
    val targetPrice: Double,
    val isAbove: Boolean, // True if we want alert when price goes ABOVE targetPrice
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

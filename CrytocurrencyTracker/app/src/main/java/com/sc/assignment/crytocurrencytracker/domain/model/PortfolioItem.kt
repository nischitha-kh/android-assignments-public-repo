package com.sc.assignment.crytocurrencytracker.domain.model

data class PortfolioItem(
    val coinId: String,
    val symbol: String,
    val name: String,
    val image: String,
    val count: Double,
    val averageBuyPrice: Double,
    val currentPrice: Double,
    val lastUpdated: Long
) {
    val totalValue: Double get() = count * currentPrice
    val totalCost: Double get() = count * averageBuyPrice
    val profitLoss: Double get() = totalValue - totalCost
    val profitLossPercentage: Double get() = if (totalCost != 0.0) (profitLoss / totalCost) * 100 else 0.0
}

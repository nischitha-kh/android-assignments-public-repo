package com.sc.assignment.crytocurrencytracker.domain.model

data class MarketChart(
    val prices: List<Pair<Long, Double>>
)

package com.sc.assignment.crytocurrencytracker.domain.model

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val description: String,
    val currentPrice: Double,
    val marketCap: Double,
    val marketCapRank: Int,
    val high24h: Double,
    val low24h: Double,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
    val circulatingSupply: Double,
    val totalSupply: Double?,
    val maxSupply: Double?
)

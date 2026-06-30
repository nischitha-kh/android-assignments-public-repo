package com.sc.assignment.crytocurrencytracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: ImageDto,
    val description: DescriptionDto,
    @SerializedName("market_cap_rank") val marketCapRank: Int,
    @SerializedName("market_data") val marketData: MarketDataDto
)

data class ImageDto(val large: String)
data class DescriptionDto(val en: String)

data class MarketDataDto(
    @SerializedName("current_price") val currentPrice: Map<String, Double>,
    @SerializedName("market_cap") val marketCap: Map<String, Double>,
    @SerializedName("high_24h") val high24h: Map<String, Double>,
    @SerializedName("low_24h") val low24h: Map<String, Double>,
    @SerializedName("price_change_24h") val priceChange24h: Double,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerializedName("circulating_supply") val circulatingSupply: Double,
    @SerializedName("total_supply") val totalSupply: Double?,
    @SerializedName("max_supply") val maxSupply: Double?
)

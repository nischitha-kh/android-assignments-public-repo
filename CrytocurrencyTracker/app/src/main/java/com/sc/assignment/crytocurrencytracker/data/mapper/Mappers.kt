package com.sc.assignment.crytocurrencytracker.data.mapper

import com.sc.assignment.crytocurrencytracker.data.local.entity.AlertEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity
import com.sc.assignment.crytocurrencytracker.data.remote.dto.CoinDetailDto
import com.sc.assignment.crytocurrencytracker.data.remote.dto.CoinDto
import com.sc.assignment.crytocurrencytracker.data.remote.dto.MarketChartDto
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.model.MarketChart
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem

fun CoinDto.toCoinEntity(): CoinEntity {
    return CoinEntity(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        priceChangePercentage24h = priceChangePercentage24h
    )
}

fun CoinEntity.toCoin(sparkline: List<Double> = emptyList()): Coin {
    return Coin(
        id = id,
        symbol = symbol,
        name = name,
        image = image,
        currentPrice = currentPrice,
        marketCap = marketCap,
        marketCapRank = marketCapRank,
        priceChangePercentage24h = priceChangePercentage24h,
        sparkline = sparkline
    )
}

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        id = id,
        symbol = symbol,
        name = name,
        image = image.large,
        description = description.en,
        currentPrice = marketData.currentPrice["usd"] ?: 0.0,
        marketCap = marketData.marketCap["usd"] ?: 0.0,
        marketCapRank = marketCapRank,
        high24h = marketData.high24h["usd"] ?: 0.0,
        low24h = marketData.low24h["usd"] ?: 0.0,
        priceChange24h = marketData.priceChange24h,
        priceChangePercentage24h = marketData.priceChangePercentage24h,
        circulatingSupply = marketData.circulatingSupply,
        totalSupply = marketData.totalSupply,
        maxSupply = marketData.maxSupply
    )
}

fun MarketChartDto.toMarketChart(): MarketChart {
    return MarketChart(
        prices = prices.map { it[0].toLong() to it[1] }
    )
}

fun PortfolioEntity.toPortfolioItem(coin: CoinEntity?): PortfolioItem {
    return PortfolioItem(
        coinId = coinId,
        symbol = coin?.symbol ?: "",
        name = coin?.name ?: "",
        image = coin?.image ?: "",
        count = count,
        averageBuyPrice = averageBuyPrice,
        currentPrice = coin?.currentPrice ?: 0.0,
        lastUpdated = lastUpdated
    )
}

fun PortfolioItem.toEntity(): PortfolioEntity {
    return PortfolioEntity(
        coinId = coinId,
        count = count,
        averageBuyPrice = averageBuyPrice
    )
}

fun AlertEntity.toAlert(): Alert {
    return Alert(
        id = id,
        coinId = coinId,
        coinSymbol = coinSymbol,
        targetPrice = targetPrice,
        isAbove = isAbove,
        isEnabled = isEnabled,
        createdAt = createdAt
    )
}

fun Alert.toEntity(): AlertEntity {
    return AlertEntity(
        id = id,
        coinId = coinId,
        coinSymbol = coinSymbol,
        targetPrice = targetPrice,
        isAbove = isAbove,
        isEnabled = isEnabled,
        createdAt = createdAt
    )
}

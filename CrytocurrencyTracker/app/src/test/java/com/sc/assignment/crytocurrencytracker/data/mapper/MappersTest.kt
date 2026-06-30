package com.sc.assignment.crytocurrencytracker.data.mapper

import com.sc.assignment.crytocurrencytracker.data.local.entity.AlertEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity
import com.sc.assignment.crytocurrencytracker.data.remote.dto.*
import com.sc.assignment.crytocurrencytracker.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class MappersTest {

    @Test
    fun `CoinDto toCoinEntity maps correctly`() {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "url",
            currentPrice = 50000.0,
            marketCap = 1000000000L,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5,
            sparklineIn7d = null
        )

        val entity = dto.toCoinEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.symbol, entity.symbol)
        assertEquals(dto.name, entity.name)
        assertEquals(dto.image, entity.image)
        assertEquals(dto.currentPrice, entity.currentPrice, 0.0)
        assertEquals(dto.marketCap, entity.marketCap)
        assertEquals(dto.marketCapRank, entity.marketCapRank)
        assertEquals(dto.priceChangePercentage24h, entity.priceChangePercentage24h, 0.0)
    }

    @Test
    fun `CoinEntity toCoin maps correctly`() {
        val entity = CoinEntity(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "url",
            currentPrice = 50000.0,
            marketCap = 1000000000L,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5
        )

        val coin = entity.toCoin()

        assertEquals(entity.id, coin.id)
        assertEquals(entity.symbol, coin.symbol)
        assertEquals(entity.name, coin.name)
        assertEquals(entity.image, coin.image)
        assertEquals(entity.currentPrice, coin.currentPrice, 0.0)
        assertEquals(entity.marketCap, coin.marketCap)
        assertEquals(entity.marketCapRank, coin.marketCapRank)
        assertEquals(entity.priceChangePercentage24h, coin.priceChangePercentage24h, 0.0)
    }

    @Test
    fun `CoinDetailDto toCoinDetail maps correctly`() {
        val dto = CoinDetailDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = ImageDto(large = "large_url"),
            description = DescriptionDto(en = "Bitcoin is..."),
            marketCapRank = 1,
            marketData = MarketDataDto(
                currentPrice = mapOf("usd" to 50000.0),
                marketCap = mapOf("usd" to 1000000000.0),
                high24h = mapOf("usd" to 51000.0),
                low24h = mapOf("usd" to 49000.0),
                priceChange24h = 1000.0,
                priceChangePercentage24h = 2.0,
                circulatingSupply = 19000000.0,
                totalSupply = 21000000.0,
                maxSupply = 21000000.0
            )
        )

        val detail = dto.toCoinDetail()

        assertEquals(dto.id, detail.id)
        assertEquals(dto.symbol, detail.symbol)
        assertEquals(dto.name, detail.name)
        assertEquals(dto.image.large, detail.image)
        assertEquals(dto.description.en, detail.description)
        assertEquals(50000.0, detail.currentPrice, 0.0)
        assertEquals(1000000000.0, detail.marketCap, 0.0)
        assertEquals(dto.marketCapRank, detail.marketCapRank)
    }

    @Test
    fun `PortfolioEntity toPortfolioItem maps correctly`() {
        val entity = PortfolioEntity(
            coinId = "bitcoin",
            count = 0.5,
            averageBuyPrice = 40000.0,
            lastUpdated = 123456789L
        )
        val coinEntity = CoinEntity(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "url",
            currentPrice = 50000.0,
            marketCap = 1000000000L,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5
        )

        val item = entity.toPortfolioItem(coinEntity)

        assertEquals(entity.coinId, item.coinId)
        assertEquals(coinEntity.symbol, item.symbol)
        assertEquals(coinEntity.name, item.name)
        assertEquals(coinEntity.image, item.image)
        assertEquals(entity.count, item.count, 0.0)
        assertEquals(entity.averageBuyPrice, item.averageBuyPrice, 0.0)
        assertEquals(coinEntity.currentPrice, item.currentPrice, 0.0)
    }

    @Test
    fun `AlertEntity toAlert and Alert toEntity map correctly`() {
        val entity = AlertEntity(
            id = 1,
            coinId = "bitcoin",
            coinSymbol = "BTC",
            targetPrice = 60000.0,
            isAbove = true,
            isEnabled = true,
            createdAt = 123456789L
        )

        val alert = entity.toAlert()
        assertEquals(entity.id, alert.id)
        assertEquals(entity.coinId, alert.coinId)
        assertEquals(entity.targetPrice, alert.targetPrice, 0.0)

        val mappedBack = alert.toEntity()
        assertEquals(entity, mappedBack)
    }
}

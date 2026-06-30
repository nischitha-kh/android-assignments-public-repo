package com.sc.assignment.crytocurrencytracker.domain.repository

import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.model.MarketChart
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(forceRefresh: Boolean = false): Flow<Result<List<Coin>>>
    fun getCoinById(id: String): Flow<Result<CoinDetail>>
    fun getMarketChart(id: String, days: Int): Flow<Result<MarketChart>>
    suspend fun searchCoins(query: String): List<Coin>
    suspend fun getCoinByIdSync(id: String): Coin?
    suspend fun refreshCoins()
}

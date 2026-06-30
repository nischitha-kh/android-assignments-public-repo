package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.CoinDao
import com.sc.assignment.crytocurrencytracker.data.mapper.toCoin
import com.sc.assignment.crytocurrencytracker.data.mapper.toCoinDetail
import com.sc.assignment.crytocurrencytracker.data.mapper.toCoinEntity
import com.sc.assignment.crytocurrencytracker.data.mapper.toMarketChart
import com.sc.assignment.crytocurrencytracker.data.remote.CoinGeckoApi
import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.model.MarketChart
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinGeckoApi,
    private val dao: CoinDao
) : CoinRepository {

    override fun getCoins(forceRefresh: Boolean): Flow<Result<List<Coin>>> = flow {
        try {
            if (forceRefresh) {
                refreshCoins()
            }
            val coinsFlow = dao.getAllCoins().map { entities ->
                Result.success(entities.map { it.toCoin() })
            }
            emitAll(coinsFlow)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(Result.failure(e))
        }
    }

    override fun getCoinById(id: String): Flow<Result<CoinDetail>> = flow {
        try {
            val dto = api.getCoinById(id)
            emit(Result.success(dto.toCoinDetail()))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(Result.failure(e))
        }
    }

    override fun getMarketChart(id: String, days: Int): Flow<Result<MarketChart>> = flow {
        try {
            val dto = api.getMarketChart(id, days = days)
            emit(Result.success(dto.toMarketChart()))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(Result.failure(e))
        }
    }

    override suspend fun searchCoins(query: String): List<Coin> {
        return dao.searchCoins(query).map { it.toCoin() }
    }

    override suspend fun getCoinByIdSync(id: String): Coin? {
        return dao.getCoinById(id)?.toCoin()
    }

    override suspend fun refreshCoins() {
        val remoteCoins = api.getCoins()
        dao.insertCoins(remoteCoins.map { it.toCoinEntity() })
    }
}

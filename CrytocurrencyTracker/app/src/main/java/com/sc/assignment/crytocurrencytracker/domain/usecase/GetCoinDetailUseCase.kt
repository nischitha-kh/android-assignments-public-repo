package com.sc.assignment.crytocurrencytracker.domain.usecase

import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.model.MarketChart
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinDetailUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    fun getDetail(id: String): Flow<Result<CoinDetail>> {
        return repository.getCoinById(id)
    }

    fun getChart(id: String, days: Int): Flow<Result<MarketChart>> {
        return repository.getMarketChart(id, days)
    }
}

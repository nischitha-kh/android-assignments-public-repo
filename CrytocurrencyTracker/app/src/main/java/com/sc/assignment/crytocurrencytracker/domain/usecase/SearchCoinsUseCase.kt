package com.sc.assignment.crytocurrencytracker.domain.usecase

import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    suspend operator fun invoke(query: String): List<Coin> {
        return repository.searchCoins(query)
    }
}

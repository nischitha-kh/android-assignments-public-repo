package com.sc.assignment.crytocurrencytracker.domain.usecase

import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagePortfolioUseCase @Inject constructor(
    private val repository: PortfolioRepository
) {
    fun getPortfolio(): Flow<List<PortfolioItem>> = repository.getPortfolioItems()

    suspend fun addItem(item: PortfolioItem) = repository.addPortfolioItem(item)

    suspend fun removeItem(coinId: String) = repository.removePortfolioItem(coinId)

    suspend fun updateItem(item: PortfolioItem) = repository.updatePortfolioItem(item)
}

package com.sc.assignment.crytocurrencytracker.domain.repository

import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getPortfolioItems(): Flow<List<PortfolioItem>>
    suspend fun addPortfolioItem(item: PortfolioItem)
    suspend fun removePortfolioItem(coinId: String)
    suspend fun updatePortfolioItem(item: PortfolioItem)
}

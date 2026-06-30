package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.CoinDao
import com.sc.assignment.crytocurrencytracker.data.local.PortfolioDao
import com.sc.assignment.crytocurrencytracker.data.mapper.toEntity
import com.sc.assignment.crytocurrencytracker.data.mapper.toPortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PortfolioRepositoryImpl @Inject constructor(
    private val portfolioDao: PortfolioDao,
    private val coinDao: CoinDao
) : PortfolioRepository {

    override fun getPortfolioItems(): Flow<List<PortfolioItem>> {
        return combine(
            portfolioDao.getPortfolioItems(),
            coinDao.getAllCoins()
        ) { portfolioEntities, coinEntities ->
            val coinMap = coinEntities.associateBy { it.id }
            portfolioEntities.map { entity ->
                entity.toPortfolioItem(coinMap[entity.coinId])
            }
        }
    }

    override suspend fun addPortfolioItem(item: PortfolioItem) {
        val existingEntity = portfolioDao.getPortfolioItemById(item.coinId)
        if (existingEntity != null) {
            val totalCount = existingEntity.count + item.count
            val totalCost = (existingEntity.count * existingEntity.averageBuyPrice) + (item.count * item.averageBuyPrice)
            val newAveragePrice = totalCost / totalCount
            
            portfolioDao.updatePortfolioItem(
                existingEntity.copy(
                    count = totalCount,
                    averageBuyPrice = newAveragePrice,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        } else {
            portfolioDao.insertPortfolioItem(item.toEntity())
        }
    }

    override suspend fun removePortfolioItem(coinId: String) {
        portfolioDao.deletePortfolioItem(coinId)
    }

    override suspend fun updatePortfolioItem(item: PortfolioItem) {
        portfolioDao.updatePortfolioItem(item.toEntity())
    }
}

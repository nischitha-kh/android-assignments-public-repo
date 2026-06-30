package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.CoinDao
import com.sc.assignment.crytocurrencytracker.data.local.PortfolioDao
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PortfolioRepositoryImplTest {

    private val portfolioDao = mockk<PortfolioDao>()
    private val coinDao = mockk<CoinDao>()
    private val repository = PortfolioRepositoryImpl(portfolioDao, coinDao)

    @Test
    fun `addPortfolioItem calculates average price for existing item`() = runBlocking {
        val existingEntity = PortfolioEntity("bitcoin", 1.0, 40000.0)
        val newItem = PortfolioItem("bitcoin", "BTC", "Bitcoin", "", 1.0, 60000.0, 50000.0, 0L)
        
        coEvery { portfolioDao.getPortfolioItemById("bitcoin") } returns existingEntity
        coEvery { portfolioDao.updatePortfolioItem(any()) } returns Unit

        repository.addPortfolioItem(newItem)

        // Expected: count = 2.0, avgPrice = (40000 + 60000) / 2 = 50000.0
        coVerify { 
            portfolioDao.updatePortfolioItem(match { 
                it.count == 2.0 && it.averageBuyPrice == 50000.0 
            }) 
        }
    }

    @Test
    fun `addPortfolioItem inserts new item if not exists`() = runBlocking {
        val newItem = PortfolioItem("bitcoin", "BTC", "Bitcoin", "", 1.0, 60000.0, 50000.0, 0L)
        
        coEvery { portfolioDao.getPortfolioItemById("bitcoin") } returns null
        coEvery { portfolioDao.insertPortfolioItem(any()) } returns Unit

        repository.addPortfolioItem(newItem)

        coVerify { portfolioDao.insertPortfolioItem(any()) }
    }
}

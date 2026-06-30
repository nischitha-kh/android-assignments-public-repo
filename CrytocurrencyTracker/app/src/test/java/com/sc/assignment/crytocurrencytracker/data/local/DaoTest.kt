package com.sc.assignment.crytocurrencytracker.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DaoTest {

    private lateinit var database: CryptoDatabase
    private lateinit var coinDao: CoinDao
    private lateinit var portfolioDao: PortfolioDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CryptoDatabase::class.java
        ).allowMainThreadQueries().build()
        coinDao = database.coinDao
        portfolioDao = database.portfolioDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert and get all coins`() = runBlocking {
        val coins = listOf(
            CoinEntity("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000, 1, 1.0)
        )
        coinDao.insertCoins(coins)
        
        val result = coinDao.getAllCoins().first()
        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
    }

    @Test
    fun `portfolio operations`() = runBlocking {
        val item = PortfolioEntity("bitcoin", 1.0, 40000.0)
        portfolioDao.insertPortfolioItem(item)
        
        var result = portfolioDao.getPortfolioItems().first()
        assertEquals(1, result.size)
        
        portfolioDao.deletePortfolioItem("bitcoin")
        result = portfolioDao.getPortfolioItems().first()
        assertEquals(0, result.size)
    }
}

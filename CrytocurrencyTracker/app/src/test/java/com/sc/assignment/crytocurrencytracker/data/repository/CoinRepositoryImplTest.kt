package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.CoinDao
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import com.sc.assignment.crytocurrencytracker.data.remote.CoinGeckoApi
import com.sc.assignment.crytocurrencytracker.data.remote.dto.CoinDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoinRepositoryImplTest {

    private val api = mockk<CoinGeckoApi>()
    private val dao = mockk<CoinDao>()
    private val repository = CoinRepositoryImpl(api, dao)

    @Test
    fun `getCoins returns data from dao`() = runBlocking {
        val entities = listOf(
            CoinEntity("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000, 1, 1.0)
        )
        every { dao.getAllCoins() } returns flowOf(entities)

        val result = repository.getCoins(forceRefresh = false).first()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("bitcoin", result.getOrNull()?.get(0)?.id)
    }

    @Test
    fun `getCoins with forceRefresh calls api and inserts into dao`() = runBlocking {
        val dtos = listOf(
            CoinDto("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000, 1, 1.0, null)
        )
        val entities = listOf(
            CoinEntity("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000, 1, 1.0)
        )
        coEvery { api.getCoins() } returns dtos
        coEvery { dao.insertCoins(any()) } returns Unit
        every { dao.getAllCoins() } returns flowOf(entities)

        repository.getCoins(forceRefresh = true).first()

        coVerify { api.getCoins() }
        coVerify { dao.insertCoins(any()) }
    }

    @Test
    fun `getCoinById fetches from api`() = runBlocking {
        val detailDto = mockk<com.sc.assignment.crytocurrencytracker.data.remote.dto.CoinDetailDto>(relaxed = true)
        coEvery { api.getCoinById("bitcoin") } returns detailDto
        
        val result = repository.getCoinById("bitcoin").first()
        
        assertTrue(result.isSuccess)
        coVerify { api.getCoinById("bitcoin") }
    }
}

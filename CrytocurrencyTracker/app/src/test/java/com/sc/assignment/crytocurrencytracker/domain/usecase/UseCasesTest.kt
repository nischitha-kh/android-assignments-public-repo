package com.sc.assignment.crytocurrencytracker.domain.usecase

import com.sc.assignment.crytocurrencytracker.domain.model.*
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.PortfolioRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UseCasesTest {

    private val coinRepo = mockk<CoinRepository>()
    private val portfolioRepo = mockk<PortfolioRepository>()
    private val alertRepo = mockk<AlertRepository>()

    @Test
    fun `GetCoinsUseCase calls repository`() = runBlocking {
        val useCase = GetCoinsUseCase(coinRepo)
        val coins = listOf(mockk<Coin>())
        every { coinRepo.getCoins(any()) } returns flowOf(Result.success(coins))

        useCase(true).collect()

        coVerify { coinRepo.getCoins(true) }
    }

    @Test
    fun `GetCoinDetailUseCase calls repository`() = runBlocking {
        val useCase = GetCoinDetailUseCase(coinRepo)
        val detail = mockk<CoinDetail>()
        every { coinRepo.getCoinById(any()) } returns flowOf(Result.success(detail))
        every { coinRepo.getMarketChart(any(), any()) } returns flowOf(Result.success(mockk()))

        useCase.getDetail("id").collect()
        useCase.getChart("id", 7).collect()

        coVerify { coinRepo.getCoinById("id") }
        coVerify { coinRepo.getMarketChart("id", 7) }
    }

    @Test
    fun `ManagePortfolioUseCase calls repository`() = runBlocking {
        val useCase = ManagePortfolioUseCase(portfolioRepo)
        val item = mockk<PortfolioItem>()

        coEvery { portfolioRepo.addPortfolioItem(any()) } returns Unit
        coEvery { portfolioRepo.removePortfolioItem(any()) } returns Unit

        useCase.addItem(item)
        useCase.removeItem("id")

        coVerify { portfolioRepo.addPortfolioItem(item) }
        coVerify { portfolioRepo.removePortfolioItem("id") }
    }

    @Test
    fun `ManageAlertsUseCase calls repository`() = runBlocking {
        val useCase = ManageAlertsUseCase(alertRepo)
        val alert = mockk<Alert>()

        coEvery { alertRepo.addAlert(any()) } returns Unit
        coEvery { alertRepo.toggleAlert(any(), any()) } returns Unit

        useCase.addAlert(alert)
        useCase.toggleAlert(1, false)

        coVerify { alertRepo.addAlert(alert) }
        coVerify { alertRepo.toggleAlert(1, false) }
    }

    @Test
    fun `SearchCoinsUseCase calls repository`() = runBlocking {
        val useCase = SearchCoinsUseCase(coinRepo)
        val coins = listOf(mockk<Coin>())
        coEvery { coinRepo.searchCoins(any()) } returns coins

        val result = useCase("query")

        assertEquals(coins, result)
        coVerify { coinRepo.searchCoins("query") }
    }
}

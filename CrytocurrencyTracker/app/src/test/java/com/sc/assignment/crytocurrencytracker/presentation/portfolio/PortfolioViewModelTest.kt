package com.sc.assignment.crytocurrencytracker.presentation.portfolio

import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManagePortfolioUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private lateinit var viewModel: PortfolioViewModel
    private val managePortfolioUseCase = mockk<ManagePortfolioUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { managePortfolioUseCase.getPortfolio() } returns flowOf(emptyList())
        viewModel = PortfolioViewModel(managePortfolioUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPortfolio updates state with items and totals`() {
        val items = listOf(
            PortfolioItem("bitcoin", "BTC", "Bitcoin", "", 1.0, 40000.0, 50000.0, 0L)
        )
        every { managePortfolioUseCase.getPortfolio() } returns flowOf(items)
        
        viewModel = PortfolioViewModel(managePortfolioUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.items.size)
        assertEquals(50000.0, viewModel.state.value.totalValue, 0.0)
        assertEquals(10000.0, viewModel.state.value.totalProfitLoss, 0.0)
    }

    @Test
    fun `removePortfolioItem calls use case`() {
        coEvery { managePortfolioUseCase.removeItem(any()) } returns Unit
        
        viewModel.removePortfolioItem("bitcoin")
        testDispatcher.scheduler.advanceUntilIdle()

        io.mockk.coVerify { managePortfolioUseCase.removeItem("bitcoin") }
    }
}

package com.sc.assignment.crytocurrencytracker.presentation.dashboard

import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import com.sc.assignment.crytocurrencytracker.domain.usecase.GetCoinsUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.SearchCoinsUseCase
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
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private val getCoinsUseCase = mockk<GetCoinsUseCase>()
    private val searchCoinsUseCase = mockk<SearchCoinsUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCoinsUseCase(any()) } returns flowOf(Result.success(emptyList()))
        viewModel = DashboardViewModel(getCoinsUseCase, searchCoinsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewmodel starts, it should fetch coins`() {
        val coins = listOf(
            Coin("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000000, 1, 1.2)
        )
        // Ensure the mock returns the coins for the initialization call
        every { getCoinsUseCase(true) } returns flowOf(Result.success(coins))
        
        viewModel = DashboardViewModel(getCoinsUseCase, searchCoinsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(coins, viewModel.state.value.coins)
        assertEquals(false, viewModel.state.value.isLoading)
    }
}

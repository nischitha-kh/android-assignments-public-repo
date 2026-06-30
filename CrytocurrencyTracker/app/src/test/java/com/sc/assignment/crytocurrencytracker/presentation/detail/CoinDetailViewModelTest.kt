package com.sc.assignment.crytocurrencytracker.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.usecase.GetCoinDetailUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManageAlertsUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManagePortfolioUseCase
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
class CoinDetailViewModelTest {

    private lateinit var viewModel: CoinDetailViewModel
    private val getCoinDetailUseCase = mockk<GetCoinDetailUseCase>()
    private val managePortfolioUseCase = mockk<ManagePortfolioUseCase>()
    private val manageAlertsUseCase = mockk<ManageAlertsUseCase>()
    private val savedStateHandle = SavedStateHandle(mapOf("coinId" to "bitcoin"))
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCoinDetailUseCase.getDetail(any()) } returns flowOf(Result.success(mockk()))
        every { getCoinDetailUseCase.getChart(any(), any()) } returns flowOf(Result.success(mockk()))
        
        viewModel = CoinDetailViewModel(
            getCoinDetailUseCase, 
            managePortfolioUseCase, 
            manageAlertsUseCase, 
            savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCoinDetail updates state on success`() {
        val detail = mockk<CoinDetail>(relaxed = true)
        every { detail.name } returns "Bitcoin"
        every { getCoinDetailUseCase.getDetail("bitcoin") } returns flowOf(Result.success(detail))

        viewModel = CoinDetailViewModel(
            getCoinDetailUseCase, 
            managePortfolioUseCase, 
            manageAlertsUseCase, 
            savedStateHandle
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Bitcoin", viewModel.state.value.coin?.name)
        assertEquals(false, viewModel.state.value.isLoading)
    }
}

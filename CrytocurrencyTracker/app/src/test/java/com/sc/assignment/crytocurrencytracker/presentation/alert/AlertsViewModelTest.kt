package com.sc.assignment.crytocurrencytracker.presentation.alert

import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManageAlertsUseCase
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
class AlertsViewModelTest {

    private lateinit var viewModel: AlertsViewModel
    private val manageAlertsUseCase = mockk<ManageAlertsUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { manageAlertsUseCase.getAlerts() } returns flowOf(emptyList())
        viewModel = AlertsViewModel(manageAlertsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAlerts updates state`() {
        val alerts = listOf(
            Alert(id = 1, coinId = "bitcoin", coinSymbol = "BTC", targetPrice = 60000.0, isAbove = true)
        )
        every { manageAlertsUseCase.getAlerts() } returns flowOf(alerts)
        
        viewModel = AlertsViewModel(manageAlertsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.size)
        assertEquals("bitcoin", viewModel.state.value[0].coinId)
    }

    @Test
    fun `toggleAlert calls use case`() {
        coEvery { manageAlertsUseCase.toggleAlert(any(), any()) } returns Unit
        
        viewModel.toggleAlert(1, false)
        testDispatcher.scheduler.advanceUntilIdle()

        io.mockk.coVerify { manageAlertsUseCase.toggleAlert(1, false) }
    }
}

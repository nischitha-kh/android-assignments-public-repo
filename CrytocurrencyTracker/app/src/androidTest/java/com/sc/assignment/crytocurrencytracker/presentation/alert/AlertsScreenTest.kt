package com.sc.assignment.crytocurrencytracker.presentation.alert

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class AlertsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<AlertsViewModel>(relaxed = true)

    @Test
    fun alertsScreen_displaysEmptyMessage() {
        every { viewModel.state } returns MutableStateFlow(emptyList())

        composeTestRule.setContent {
            AlertsScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("You haven\'t set any price alerts yet. Go to a coin\'s details to set one!").assertExists()
    }

    @Test
    fun alertsScreen_displaysAlerts() {
        val alerts = listOf(
            Alert(id = 1, coinId = "bitcoin", coinSymbol = "BTC", targetPrice = 60000.0, isAbove = true)
        )
        every { viewModel.state } returns MutableStateFlow(alerts)

        composeTestRule.setContent {
            AlertsScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("BTC").assertExists()
        composeTestRule.onNodeWithText("Above $60000.0").assertExists()
    }
}

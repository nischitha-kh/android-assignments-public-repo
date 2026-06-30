package com.sc.assignment.crytocurrencytracker.presentation.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import com.sc.assignment.crytocurrencytracker.domain.model.Coin
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<DashboardViewModel>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun dashboardScreen_displaysCoins() {
        val coins = listOf(
            Coin("bitcoin", "btc", "Bitcoin", "", 50000.0, 1000000, 1, 1.2)
        )
        val state = DashboardState(coins = coins)
        every { viewModel.state } returns MutableStateFlow(state)

        composeTestRule.setContent {
            DashboardScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Bitcoin").assertExists()
        composeTestRule.onNodeWithText("BTC").assertExists()
    }

    @Test
    fun dashboardScreen_searchTriggersEvent() {
        val state = DashboardState()
        every { viewModel.state } returns MutableStateFlow(state)

        composeTestRule.setContent {
            DashboardScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Search coins...").performTextInput("eth")
        
        io.mockk.verify { viewModel.onEvent(DashboardEvent.SearchQueryChanged("eth")) }
    }
}

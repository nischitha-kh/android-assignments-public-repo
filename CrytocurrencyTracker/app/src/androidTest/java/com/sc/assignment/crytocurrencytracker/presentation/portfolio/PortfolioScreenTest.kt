package com.sc.assignment.crytocurrencytracker.presentation.portfolio

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class PortfolioScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<PortfolioViewModel>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun portfolioScreen_displaysEmptyMessage() {
        every { viewModel.state } returns MutableStateFlow(PortfolioState(items = emptyList()))

        composeTestRule.setContent {
            PortfolioScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Your portfolio is empty. Add coins from the details screen to start tracking your assets!").assertExists()
    }

    @Test
    fun portfolioScreen_displaysHoldings() {
        val items = listOf(
            PortfolioItem("bitcoin", "BTC", "Bitcoin", "", 1.5, 40000.0, 50000.0, 0L)
        )
        val state = PortfolioState(items = items, totalValue = 75000.0, totalProfitLoss = 15000.0)
        every { viewModel.state } returns MutableStateFlow(state)

        composeTestRule.setContent {
            PortfolioScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Bitcoin").assertExists()
        composeTestRule.onNodeWithText("Count: 1.5 | Avg Buy: $40000.00").assertExists()
        composeTestRule.onNodeWithText("$75000.00").assertExists()
    }
}

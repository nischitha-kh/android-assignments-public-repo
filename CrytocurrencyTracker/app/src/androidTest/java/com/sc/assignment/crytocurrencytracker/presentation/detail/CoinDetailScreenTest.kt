package com.sc.assignment.crytocurrencytracker.presentation.detail

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CoinDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<CoinDetailViewModel>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true)

    @Test
    fun coinDetailScreen_displaysCoinInfo() {
        val detail = CoinDetail(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "",
            description = "Bitcoin description",
            currentPrice = 50000.0,
            marketCap = 1000000000.0,
            marketCapRank = 1,
            high24h = 51000.0,
            low24h = 49000.0,
            priceChange24h = 1000.0,
            priceChangePercentage24h = 2.0,
            circulatingSupply = 19000000.0,
            totalSupply = 21000000.0,
            maxSupply = 21000000.0
        )
        val state = CoinDetailState(coin = detail, isLoading = false)
        every { viewModel.state } returns MutableStateFlow(state)

        composeTestRule.setContent {
            CoinDetailScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Bitcoin").assertExists()
        composeTestRule.onNodeWithText("$50000.00").assertExists()
        composeTestRule.onNodeWithText("Market Cap Rank").assertExists()
    }

    @Test
    fun coinDetailScreen_showsPortfolioDialog() {
        val detail = mockk<CoinDetail>(relaxed = true)
        val state = CoinDetailState(coin = detail, isLoading = false)
        every { viewModel.state } returns MutableStateFlow(state)

        composeTestRule.setContent {
            CoinDetailScreen(navController = navController, viewModel = viewModel)
        }

        composeTestRule.onNodeWithText("Add to Portfolio").performClick()
        
        composeTestRule.onNodeWithText("Amount").assertExists()
        composeTestRule.onNodeWithText("Buy Price ($)").assertExists()
    }
}

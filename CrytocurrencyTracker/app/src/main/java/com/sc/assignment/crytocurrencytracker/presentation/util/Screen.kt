package com.sc.assignment.crytocurrencytracker.presentation.util

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object CoinDetail : Screen("coin_detail/{coinId}") {
        fun createRoute(coinId: String) = "coin_detail/$coinId"
    }
    object Portfolio : Screen("portfolio")
    object Alerts : Screen("alerts")
}

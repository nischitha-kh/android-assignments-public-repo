package com.sc.assignment.crytocurrencytracker.presentation.dashboard

import com.sc.assignment.crytocurrencytracker.domain.model.Coin

data class DashboardState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false
)

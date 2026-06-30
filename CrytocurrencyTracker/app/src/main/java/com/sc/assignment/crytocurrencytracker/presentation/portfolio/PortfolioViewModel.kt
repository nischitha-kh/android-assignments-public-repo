package com.sc.assignment.crytocurrencytracker.presentation.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManagePortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val managePortfolioUseCase: ManagePortfolioUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PortfolioState())
    val state: StateFlow<PortfolioState> = _state.asStateFlow()

    init {
        getPortfolio()
    }

    private fun getPortfolio() {
        managePortfolioUseCase.getPortfolio().onEach { items ->
            val totalValue = items.sumOf { it.totalValue }
            val totalProfitLoss = items.sumOf { it.profitLoss }
            _state.update { it.copy(
                items = items,
                totalValue = totalValue,
                totalProfitLoss = totalProfitLoss
            ) }
        }.launchIn(viewModelScope)
    }

    fun addPortfolioItem(coinId: String, symbol: String, name: String, image: String, count: Double, buyPrice: Double) {
        viewModelScope.launch {
            val item = PortfolioItem(
                coinId = coinId,
                symbol = symbol,
                name = name,
                image = image,
                count = count,
                averageBuyPrice = buyPrice,
                currentPrice = 0.0, // Will be updated by repository
                lastUpdated = System.currentTimeMillis()
            )
            managePortfolioUseCase.addItem(item)
        }
    }

    fun removePortfolioItem(coinId: String) {
        viewModelScope.launch {
            managePortfolioUseCase.removeItem(coinId)
        }
    }
}

data class PortfolioState(
    val items: List<PortfolioItem> = emptyList(),
    val totalValue: Double = 0.0,
    val totalProfitLoss: Double = 0.0
)

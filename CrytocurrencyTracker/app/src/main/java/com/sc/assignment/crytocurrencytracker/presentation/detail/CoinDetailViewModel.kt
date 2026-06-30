package com.sc.assignment.crytocurrencytracker.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sc.assignment.crytocurrencytracker.domain.model.CoinDetail
import com.sc.assignment.crytocurrencytracker.domain.model.MarketChart
import com.sc.assignment.crytocurrencytracker.domain.model.PortfolioItem
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.usecase.GetCoinDetailUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManagePortfolioUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManageAlertsUseCase
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
class CoinDetailViewModel @Inject constructor(
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val managePortfolioUseCase: ManagePortfolioUseCase,
    private val manageAlertsUseCase: ManageAlertsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CoinDetailState())
    val state: StateFlow<CoinDetailState> = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("coinId")?.let { coinId ->
            getCoinDetail(coinId)
            getMarketChart(coinId)
        }
    }

    private fun getCoinDetail(id: String) {
        getCoinDetailUseCase.getDetail(id).onEach { result ->
            result.onSuccess { detail ->
                _state.update { it.copy(coin = detail, isLoading = false, error = null) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMarketChart(id: String) {
        getCoinDetailUseCase.getChart(id, 7).onEach { result ->
            result.onSuccess { chart ->
                _state.update { it.copy(chart = chart) }
            }
        }.launchIn(viewModelScope)
    }

    fun addToPortfolio(count: Double, buyPrice: Double) {
        val coin = _state.value.coin ?: return
        viewModelScope.launch {
            managePortfolioUseCase.addItem(
                PortfolioItem(
                    coinId = coin.id,
                    symbol = coin.symbol,
                    name = coin.name,
                    image = coin.image,
                    count = count,
                    averageBuyPrice = buyPrice,
                    currentPrice = coin.currentPrice,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    fun setAlert(targetPrice: Double, isAbove: Boolean) {
        val coin = _state.value.coin ?: return
        viewModelScope.launch {
            manageAlertsUseCase.addAlert(
                Alert(
                    coinId = coin.id,
                    coinSymbol = coin.symbol,
                    targetPrice = targetPrice,
                    isAbove = isAbove
                )
            )
        }
    }
}

data class CoinDetailState(
    val coin: CoinDetail? = null,
    val chart: MarketChart? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

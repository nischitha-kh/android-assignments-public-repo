package com.sc.assignment.crytocurrencytracker.presentation.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.usecase.ManageAlertsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val manageAlertsUseCase: ManageAlertsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<List<Alert>>(emptyList())
    val state: StateFlow<List<Alert>> = _state.asStateFlow()

    init {
        getAlerts()
    }

    private fun getAlerts() {
        manageAlertsUseCase.getAlerts().onEach { alerts ->
            _state.value = alerts
        }.launchIn(viewModelScope)
    }

    fun addAlert(coinId: String, symbol: String, targetPrice: Double, isAbove: Boolean) {
        viewModelScope.launch {
            manageAlertsUseCase.addAlert(
                Alert(
                    coinId = coinId,
                    coinSymbol = symbol,
                    targetPrice = targetPrice,
                    isAbove = isAbove
                )
            )
        }
    }

    fun removeAlert(id: Int) {
        viewModelScope.launch {
            manageAlertsUseCase.removeAlert(id)
        }
    }

    fun toggleAlert(id: Int, enabled: Boolean) {
        viewModelScope.launch {
            manageAlertsUseCase.toggleAlert(id, enabled)
        }
    }
}

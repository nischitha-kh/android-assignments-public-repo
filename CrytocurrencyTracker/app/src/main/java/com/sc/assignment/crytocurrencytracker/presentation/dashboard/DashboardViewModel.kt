package com.sc.assignment.crytocurrencytracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sc.assignment.crytocurrencytracker.domain.usecase.GetCoinsUseCase
import com.sc.assignment.crytocurrencytracker.domain.usecase.SearchCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val searchCoinsUseCase: SearchCoinsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        getCoins(forceRefresh = true)
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.Refresh -> getCoins(forceRefresh = true)
            is DashboardEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    searchCoins(event.query)
                }
            }
        }
    }

    private fun getCoins(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            _state.update { it.copy(isLoading = true) }
        }
        getCoinsUseCase(forceRefresh).onEach { result ->
            result.onSuccess { coins ->
                _state.update { it.copy(coins = coins, isLoading = false, error = null) }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun searchCoins(query: String) {
        if (query.isBlank()) {
            getCoins()
            return
        }
        val results = searchCoinsUseCase(query)
        _state.update { it.copy(coins = results) }
    }
}

sealed class DashboardEvent {
    object Refresh : DashboardEvent()
    data class SearchQueryChanged(val query: String) : DashboardEvent()
}

package com.sc.assignment.crytocurrencytracker.domain.usecase

import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageAlertsUseCase @Inject constructor(
    private val repository: AlertRepository
) {
    fun getAlerts(): Flow<List<Alert>> = repository.getAlerts()

    suspend fun addAlert(alert: Alert) = repository.addAlert(alert)

    suspend fun removeAlert(id: Int) = repository.removeAlert(id)

    suspend fun toggleAlert(id: Int, enabled: Boolean) = repository.toggleAlert(id, enabled)
}

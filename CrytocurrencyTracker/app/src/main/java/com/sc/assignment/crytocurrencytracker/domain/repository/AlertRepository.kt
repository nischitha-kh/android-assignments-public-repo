package com.sc.assignment.crytocurrencytracker.domain.repository

import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAlerts(): Flow<List<Alert>>
    suspend fun addAlert(alert: Alert)
    suspend fun removeAlert(id: Int)
    suspend fun toggleAlert(id: Int, enabled: Boolean)
    suspend fun getEnabledAlerts(): List<Alert>
}

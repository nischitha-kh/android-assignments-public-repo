package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.AlertDao
import com.sc.assignment.crytocurrencytracker.data.mapper.toAlert
import com.sc.assignment.crytocurrencytracker.data.mapper.toEntity
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val dao: AlertDao
) : AlertRepository {

    override fun getAlerts(): Flow<List<Alert>> {
        return dao.getAllAlerts().map { entities ->
            entities.map { it.toAlert() }
        }
    }

    override suspend fun addAlert(alert: Alert) {
        dao.insertAlert(alert.toEntity())
    }

    override suspend fun removeAlert(id: Int) {
        dao.deleteAlert(id)
    }

    override suspend fun toggleAlert(id: Int, enabled: Boolean) {
        dao.updateAlertStatus(id, enabled)
    }

    override suspend fun getEnabledAlerts(): List<Alert> {
        return dao.getEnabledAlerts().map { it.toAlert() }
    }
}

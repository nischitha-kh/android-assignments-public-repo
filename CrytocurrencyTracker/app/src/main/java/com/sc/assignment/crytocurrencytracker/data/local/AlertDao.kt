package com.sc.assignment.crytocurrencytracker.data.local

import androidx.room.*
import com.sc.assignment.crytocurrencytracker.data.local.entity.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("DELETE FROM alerts WHERE id = :id")
    suspend fun deleteAlert(id: Int)

    @Query("UPDATE alerts SET isEnabled = :enabled WHERE id = :id")
    suspend fun updateAlertStatus(id: Int, enabled: Boolean)

    @Query("SELECT * FROM alerts WHERE isEnabled = 1")
    suspend fun getEnabledAlerts(): List<AlertEntity>
}

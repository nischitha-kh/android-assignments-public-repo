package com.sc.assignment.crytocurrencytracker.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sc.assignment.crytocurrencytracker.R
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coinRepository: CoinRepository,
    private val alertRepository: AlertRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Starting background data synchronization")
            coinRepository.refreshCoins()
            Log.d("SyncWorker", "Coins refreshed from API")
            checkAlerts()
            Log.d("SyncWorker", "Sync successfully completed")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed error: ${e.message}", e)
            Result.retry()
        }
    }

    private suspend fun checkAlerts() {
        Log.d("SyncWorker", "Checking for enabled alerts...")
        val enabledAlerts = alertRepository.getEnabledAlerts()
        Log.d("SyncWorker", "Found ${enabledAlerts.size} enabled alerts")
        if (enabledAlerts.isEmpty()) return

        enabledAlerts.forEach { alert ->
            Log.d("SyncWorker", "Processing alert: ID=${alert.id}, CoinId=${alert.coinId}, Target=${alert.targetPrice}")
            val coin = coinRepository.getCoinByIdSync(alert.coinId)
            if (coin != null) {
                val price = coin.currentPrice
                val triggered = if (alert.isAbove) {
                    price >= alert.targetPrice
                } else {
                    price <= alert.targetPrice
                }
                
                Log.d("SyncWorker", "Alert comparison for ${alert.coinSymbol}: CurrentPrice=$price, Target=${alert.targetPrice}, IsAbove=${alert.isAbove}, Triggered=$triggered")
                
                if (triggered) {
                    showNotification(alert.coinSymbol, price, alert.targetPrice, alert.isAbove)
                    alertRepository.toggleAlert(alert.id, false)
                    Log.d("SyncWorker", "Notification triggered and alert disabled for ${alert.coinSymbol}")
                }
            } else {
                Log.w("SyncWorker", "CRITICAL: Coin '${alert.coinId}' not found in database. Alert cannot be processed.")
            }
        }
    }

    private fun showNotification(symbol: String, currentPrice: Double, targetPrice: Double, isAbove: Boolean) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "price_alerts"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, 
                "Price Alerts", 
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for cryptocurrency price thresholds"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val direction = if (isAbove) "above" else "below"
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Price Alert triggered!")
            .setContentText("${symbol.uppercase()} is now $direction $$targetPrice (Current: $$currentPrice)")
            .setSmallIcon(R.mipmap.ic_launcher) // Use app icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

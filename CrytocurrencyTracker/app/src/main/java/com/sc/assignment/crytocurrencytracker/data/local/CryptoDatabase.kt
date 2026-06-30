package com.sc.assignment.crytocurrencytracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sc.assignment.crytocurrencytracker.data.local.entity.AlertEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity

@Database(
    entities = [CoinEntity::class, PortfolioEntity::class, AlertEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {
    abstract val coinDao: CoinDao
    abstract val portfolioDao: PortfolioDao
    abstract val alertDao: AlertDao

    companion object {
        const val DATABASE_NAME = "crypto_db"
    }
}

package com.sc.assignment.crytocurrencytracker.di

import android.content.Context
import androidx.room.Room
import com.sc.assignment.crytocurrencytracker.data.local.AlertDao
import com.sc.assignment.crytocurrencytracker.data.local.CoinDao
import com.sc.assignment.crytocurrencytracker.data.local.CryptoDatabase
import com.sc.assignment.crytocurrencytracker.data.local.PortfolioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCryptoDatabase(@ApplicationContext context: Context): CryptoDatabase {
        return Room.databaseBuilder(
            context,
            CryptoDatabase::class.java,
            CryptoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCoinDao(database: CryptoDatabase): CoinDao = database.coinDao

    @Provides
    fun providePortfolioDao(database: CryptoDatabase): PortfolioDao = database.portfolioDao

    @Provides
    fun provideAlertDao(database: CryptoDatabase): AlertDao = database.alertDao
}

package com.sc.assignment.crytocurrencytracker.di

import com.sc.assignment.crytocurrencytracker.data.repository.AlertRepositoryImpl
import com.sc.assignment.crytocurrencytracker.data.repository.CoinRepositoryImpl
import com.sc.assignment.crytocurrencytracker.data.repository.PortfolioRepositoryImpl
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.PortfolioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        coinRepositoryImpl: CoinRepositoryImpl
    ): CoinRepository

    @Binds
    @Singleton
    abstract fun bindPortfolioRepository(
        portfolioRepositoryImpl: PortfolioRepositoryImpl
    ): PortfolioRepository

    @Binds
    @Singleton
    abstract fun bindAlertRepository(
        alertRepositoryImpl: AlertRepositoryImpl
    ): AlertRepository
}

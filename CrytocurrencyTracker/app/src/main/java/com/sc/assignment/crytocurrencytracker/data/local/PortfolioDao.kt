package com.sc.assignment.crytocurrencytracker.data.local

import androidx.room.*
import com.sc.assignment.crytocurrencytracker.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio")
    fun getPortfolioItems(): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolioItem(item: PortfolioEntity)

    @Query("SELECT * FROM portfolio WHERE coinId = :coinId")
    suspend fun getPortfolioItemById(coinId: String): PortfolioEntity?

    @Query("DELETE FROM portfolio WHERE coinId = :coinId")
    suspend fun deletePortfolioItem(coinId: String)

    @Update
    suspend fun updatePortfolioItem(item: PortfolioEntity)
}

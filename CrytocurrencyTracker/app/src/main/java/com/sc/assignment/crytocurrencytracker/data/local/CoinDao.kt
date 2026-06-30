package com.sc.assignment.crytocurrencytracker.data.local

import androidx.room.*
import com.sc.assignment.crytocurrencytracker.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%'")
    suspend fun searchCoins(query: String): List<CoinEntity>

    @Query("SELECT * FROM coins WHERE id = :id")
    suspend fun getCoinById(id: String): CoinEntity?

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()
}

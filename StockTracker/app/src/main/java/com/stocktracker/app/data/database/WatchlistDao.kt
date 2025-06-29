package com.stocktracker.app.data.database

import androidx.room.*
import com.stocktracker.app.data.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getWatchlist(): Flow<List<WatchlistItem>>
    
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE symbol = :symbol)")
    suspend fun isInWatchlist(symbol: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistItem)
    
    @Query("DELETE FROM watchlist WHERE symbol = :symbol")
    suspend fun removeFromWatchlist(symbol: String)
}
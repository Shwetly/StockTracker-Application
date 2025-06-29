package com.stocktracker.app.data.database

import androidx.room.*
import com.stocktracker.app.data.model.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): Stock?
    
    @Query("SELECT * FROM stocks")
    fun getAllStocks(): Flow<List<Stock>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stock)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(stocks: List<Stock>)
    
    @Delete
    suspend fun deleteStock(stock: Stock)
    
    @Query("DELETE FROM stocks WHERE lastUpdated < :timestamp")
    suspend fun deleteOldStocks(timestamp: Long)
}
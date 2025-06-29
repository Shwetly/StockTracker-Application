package com.stocktracker.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.stocktracker.app.data.model.Stock
import com.stocktracker.app.data.model.WatchlistItem

@Database(
    entities = [Stock::class, WatchlistItem::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun watchlistDao(): WatchlistDao
    
    companion object {
        const val DATABASE_NAME = "stock_database"
    }
}
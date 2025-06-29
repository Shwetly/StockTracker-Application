package com.stocktracker.app.di

import android.content.Context
import androidx.room.Room
import com.stocktracker.app.data.database.StockDatabase
import com.stocktracker.app.data.database.StockDao
import com.stocktracker.app.data.database.WatchlistDao
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
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            StockDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideStockDao(database: StockDatabase): StockDao {
        return database.stockDao()
    }
    
    @Provides
    fun provideWatchlistDao(database: StockDatabase): WatchlistDao {
        return database.watchlistDao()
    }
}
package com.stocktracker.app.data.repository

import com.stocktracker.app.data.model.*
import com.stocktracker.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun searchStocks(query: String): Resource<List<SearchResult>>
    suspend fun getStockQuote(symbol: String): Resource<StockQuote>
    suspend fun getStockTimeSeries(symbol: String): Resource<List<ChartDataPoint>>
    fun getWatchlist(): Flow<List<WatchlistItem>>
    suspend fun addToWatchlist(symbol: String)
    suspend fun removeFromWatchlist(symbol: String)
    suspend fun isInWatchlist(symbol: String): Boolean
}
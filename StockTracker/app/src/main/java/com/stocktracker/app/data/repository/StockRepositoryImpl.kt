package com.stocktracker.app.data.repository

import com.stocktracker.app.data.api.AlphaVantageApi
import com.stocktracker.app.data.database.StockDao
import com.stocktracker.app.data.database.WatchlistDao
import com.stocktracker.app.data.model.*
import com.stocktracker.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: AlphaVantageApi,
    private val stockDao: StockDao,
    private val watchlistDao: WatchlistDao
) : StockRepository {
    
    override suspend fun searchStocks(query: String): Resource<List<SearchResult>> {
        return try {
            val response = api.searchStocks(keywords = query, apiKey = AlphaVantageApi.API_KEY)
            if (response.isSuccessful) {
                val searchResults = response.body()?.bestMatches?.map { dto ->
                    SearchResult(
                        symbol = dto.symbol,
                        name = dto.name,
                        type = dto.type,
                        region = dto.region,
                        marketOpen = dto.marketOpen,
                        marketClose = dto.marketClose,
                        timezone = dto.timezone,
                        currency = dto.currency,
                        matchScore = dto.matchScore
                    )
                } ?: emptyList()
                Resource.Success(searchResults)
            } else {
                Resource.Error("Failed to search stocks: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }
    
    override suspend fun getStockQuote(symbol: String): Resource<StockQuote> {
        return try {
            val response = api.getStockQuote(symbol = symbol, apiKey = AlphaVantageApi.API_KEY)
            if (response.isSuccessful) {
                val quote = response.body()?.globalQuote?.let { dto ->
                    StockQuote(
                        symbol = dto.symbol,
                        open = dto.open,
                        high = dto.high,
                        low = dto.low,
                        price = dto.price,
                        volume = dto.volume,
                        latestTradingDay = dto.latestTradingDay,
                        previousClose = dto.previousClose,
                        change = dto.change,
                        changePercent = dto.changePercent
                    )
                }
                if (quote != null) {
                    Resource.Success(quote)
                } else {
                    Resource.Error("Stock not found")
                }
            } else {
                Resource.Error("Failed to get stock quote: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }
    
    override suspend fun getStockTimeSeries(symbol: String): Resource<List<ChartDataPoint>> {
        return try {
            val response = api.getTimeSeries(symbol = symbol, apiKey = AlphaVantageApi.API_KEY)
            if (response.isSuccessful) {
                val chartData = response.body()?.timeSeries?.entries
                    ?.take(30) // Last 30 days
                    ?.map { (date, data) ->
                        ChartDataPoint(
                            date = date,
                            price = data.close.toDouble(),
                            volume = data.volume.toLong()
                        )
                    }
                    ?.reversed() ?: emptyList()
                Resource.Success(chartData)
            } else {
                Resource.Error("Failed to get time series: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.message}")
        }
    }
    
    override fun getWatchlist(): Flow<List<WatchlistItem>> {
        return watchlistDao.getWatchlist()
    }
    
    override suspend fun addToWatchlist(symbol: String) {
        watchlistDao.addToWatchlist(WatchlistItem(symbol))
    }
    
    override suspend fun removeFromWatchlist(symbol: String) {
        watchlistDao.removeFromWatchlist(symbol)
    }
    
    override suspend fun isInWatchlist(symbol: String): Boolean {
        return watchlistDao.isInWatchlist(symbol)
    }
}
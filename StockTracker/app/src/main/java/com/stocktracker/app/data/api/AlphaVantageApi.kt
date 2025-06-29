package com.stocktracker.app.data.api

import com.stocktracker.app.data.api.response.GlobalQuoteResponse
import com.stocktracker.app.data.api.response.SearchResponse
import com.stocktracker.app.data.api.response.TimeSeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageApi {
    
    @GET("query")
    suspend fun searchStocks(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String
    ): Response<SearchResponse>
    
    @GET("query")
    suspend fun getStockQuote(
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): Response<GlobalQuoteResponse>
    
    @GET("query")
    suspend fun getTimeSeries(
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): Response<TimeSeriesResponse>
    
    companion object {
        const val BASE_URL = "https://www.alphavantage.co/"
        const val API_KEY = "YOUR_API_KEY_HERE" // Replace with your actual API key
    }
}
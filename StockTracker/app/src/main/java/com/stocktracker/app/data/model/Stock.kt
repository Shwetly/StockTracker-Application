package com.stocktracker.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey
    val symbol: String,
    val name: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val volume: Long? = null,
    val marketCap: String? = null,
    val peRatio: Double? = null,
    val high52Week: Double? = null,
    val low52Week: Double? = null,
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class StockQuote(
    val symbol: String,
    val open: String,
    val high: String,
    val low: String,
    val price: String,
    val volume: String,
    val latestTradingDay: String,
    val previousClose: String,
    val change: String,
    val changePercent: String
) : Parcelable

@Parcelize
data class SearchResult(
    val symbol: String,
    val name: String,
    val type: String,
    val region: String,
    val marketOpen: String,
    val marketClose: String,
    val timezone: String,
    val currency: String,
    val matchScore: String
) : Parcelable

@Parcelize
data class ChartDataPoint(
    val date: String,
    val price: Double,
    val volume: Long
) : Parcelable

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey
    val symbol: String,
    val addedAt: Long = System.currentTimeMillis()
)
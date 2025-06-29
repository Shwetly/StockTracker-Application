package com.stocktracker.app.data.api.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("bestMatches")
    val bestMatches: List<SearchResultDto>
)

data class SearchResultDto(
    @SerializedName("1. symbol")
    val symbol: String,
    @SerializedName("2. name")
    val name: String,
    @SerializedName("3. type")
    val type: String,
    @SerializedName("4. region")
    val region: String,
    @SerializedName("5. marketOpen")
    val marketOpen: String,
    @SerializedName("6. marketClose")
    val marketClose: String,
    @SerializedName("7. timezone")
    val timezone: String,
    @SerializedName("8. currency")
    val currency: String,
    @SerializedName("9. matchScore")
    val matchScore: String
)

data class GlobalQuoteResponse(
    @SerializedName("Global Quote")
    val globalQuote: GlobalQuoteDto
)

data class GlobalQuoteDto(
    @SerializedName("01. symbol")
    val symbol: String,
    @SerializedName("02. open")
    val open: String,
    @SerializedName("03. high")
    val high: String,
    @SerializedName("04. low")
    val low: String,
    @SerializedName("05. price")
    val price: String,
    @SerializedName("06. volume")
    val volume: String,
    @SerializedName("07. latest trading day")
    val latestTradingDay: String,
    @SerializedName("08. previous close")
    val previousClose: String,
    @SerializedName("09. change")
    val change: String,
    @SerializedName("10. change percent")
    val changePercent: String
)

data class TimeSeriesResponse(
    @SerializedName("Meta Data")
    val metaData: MetaDataDto,
    @SerializedName("Time Series (Daily)")
    val timeSeries: Map<String, TimeSeriesDataDto>
)

data class MetaDataDto(
    @SerializedName("1. Information")
    val information: String,
    @SerializedName("2. Symbol")
    val symbol: String,
    @SerializedName("3. Last Refreshed")
    val lastRefreshed: String,
    @SerializedName("4. Time Zone")
    val timeZone: String
)

data class TimeSeriesDataDto(
    @SerializedName("1. open")
    val open: String,
    @SerializedName("2. high")
    val high: String,
    @SerializedName("3. low")
    val low: String,
    @SerializedName("4. close")
    val close: String,
    @SerializedName("5. volume")
    val volume: String
)
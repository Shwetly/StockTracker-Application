package com.stocktracker.app.presentation.screens.stock_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocktracker.app.data.model.ChartDataPoint
import com.stocktracker.app.data.repository.StockRepository
import com.stocktracker.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(StockDetailState())
    val state: StateFlow<StockDetailState> = _state.asStateFlow()
    
    fun loadStockData(symbol: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // Load stock quote
            when (val quoteResult = repository.getStockQuote(symbol)) {
                is Resource.Success -> {
                    val quote = quoteResult.data
                    if (quote != null) {
                        _state.value = _state.value.copy(
                            currentPrice = quote.price.toDoubleOrNull() ?: 0.0,
                            priceChange = quote.change.toDoubleOrNull() ?: 0.0,
                            changePercent = quote.changePercent.replace("%", "").toDoubleOrNull() ?: 0.0
                        )
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(error = quoteResult.message)
                }
                is Resource.Loading -> { /* Handle loading */ }
            }
            
            // Load chart data
            when (val chartResult = repository.getStockTimeSeries(symbol)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        chartData = chartResult.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = chartResult.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> { /* Handle loading */ }
            }
        }
    }
    
    fun onPeriodSelected(period: String) {
        _state.value = _state.value.copy(selectedPeriod = period)
        // Reload chart data for selected period
    }
    
    fun addToWatchlist(symbol: String, watchlistName: String) {
        viewModelScope.launch {
            repository.addToWatchlist(symbol)
            _state.value = _state.value.copy(isInWatchlist = true)
        }
    }
}

data class StockDetailState(
    val currentPrice: Double = 0.0,
    val priceChange: Double = 0.0,
    val changePercent: Double = 0.0,
    val chartData: List<ChartDataPoint> = emptyList(),
    val selectedPeriod: String = "1D",
    val isInWatchlist: Boolean = false,
    val availableWatchlists: List<String> = listOf("Watchlist 1", "Watchlist 2"),
    val isLoading: Boolean = false,
    val error: String? = null
)
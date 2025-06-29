package com.stocktracker.app.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocktracker.app.data.model.SearchResult
import com.stocktracker.app.data.model.Stock
import com.stocktracker.app.data.repository.StockRepository
import com.stocktracker.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        loadMarketData()
    }
    
    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        
        searchJob?.cancel()
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                searchStocks(query)
            }
        } else {
            _state.value = _state.value.copy(searchResults = emptyList())
        }
    }
    
    private suspend fun searchStocks(query: String) {
        _state.value = _state.value.copy(isSearching = true)
        
        when (val result = repository.searchStocks(query)) {
            is Resource.Success -> {
                _state.value = _state.value.copy(
                    searchResults = result.data ?: emptyList(),
                    isSearching = false,
                    error = null
                )
            }
            is Resource.Error -> {
                _state.value = _state.value.copy(
                    isSearching = false,
                    error = result.message
                )
            }
            is Resource.Loading -> {
                _state.value = _state.value.copy(isSearching = true)
            }
        }
    }
    
    private fun loadMarketData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingMarketData = true)
            
            // Simulate loading top gainers and losers
            // In real implementation, you would call repository.getTopGainersLosers()
            delay(1000)
            
            val mockGainers = listOf(
                Stock("AAPL", "Apple Inc.", 150.25, 5.25, 3.62, 1000000),
                Stock("GOOGL", "Alphabet Inc.", 2750.80, 45.30, 1.67, 800000),
                Stock("MSFT", "Microsoft Corp.", 305.15, 8.90, 3.01, 1200000),
                Stock("TSLA", "Tesla Inc.", 850.45, 25.60, 3.10, 900000)
            )
            
            val mockLosers = listOf(
                Stock("META", "Meta Platforms", 320.75, -12.45, -3.74, 1100000),
                Stock("NFLX", "Netflix Inc.", 385.20, -8.80, -2.23, 700000),
                Stock("NVDA", "NVIDIA Corp.", 220.30, -6.70, -2.95, 950000),
                Stock("AMD", "Advanced Micro", 95.85, -3.15, -3.18, 850000)
            )
            
            _state.value = _state.value.copy(
                topGainers = mockGainers,
                topLosers = mockLosers,
                isLoadingMarketData = false
            )
        }
    }
    
    fun clearSearch() {
        _state.value = _state.value.copy(
            searchQuery = "",
            searchResults = emptyList()
        )
    }
}

data class HomeState(
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val topGainers: List<Stock> = emptyList(),
    val topLosers: List<Stock> = emptyList(),
    val isLoadingMarketData: Boolean = false,
    val error: String? = null
)
package com.stocktracker.app.presentation.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stocktracker.app.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(WatchlistState())
    val state: StateFlow<WatchlistState> = _state.asStateFlow()
    
    init {
        loadWatchlists()
    }
    
    private fun loadWatchlists() {
        viewModelScope.launch {
            // Mock data for now
            _state.value = _state.value.copy(
                watchlists = listOf("Watchlist 1", "Watchlist 2")
            )
        }
    }
}

data class WatchlistState(
    val watchlists: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
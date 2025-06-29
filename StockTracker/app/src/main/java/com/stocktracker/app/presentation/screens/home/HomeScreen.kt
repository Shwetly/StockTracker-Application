package com.stocktracker.app.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stocktracker.app.presentation.components.SearchBar
import com.stocktracker.app.presentation.components.StockCard
import com.stocktracker.app.presentation.components.TopGainersLosersSection
import com.stocktracker.app.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStockClick: (String, String) -> Unit,
    onWatchlistClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Stocks App",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Search action */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "Home",
                onTabSelected = { tab ->
                    if (tab == "Watchlist") {
                        onWatchlistClick()
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            item {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    searchResults = state.searchResults,
                    isLoading = state.isSearching,
                    onStockSelect = { symbol, name ->
                        onStockClick(symbol, name)
                        viewModel.clearSearch()
                    }
                )
            }
            
            // Top Gainers Section
            item {
                TopGainersLosersSection(
                    title = "Top Gainers",
                    stocks = state.topGainers,
                    isLoading = state.isLoadingMarketData,
                    onStockClick = onStockClick,
                    onViewAllClick = { /* Navigate to full list */ }
                )
            }
            
            // Top Losers Section
            item {
                TopGainersLosersSection(
                    title = "Top Losers",
                    stocks = state.topLosers,
                    isLoading = state.isLoadingMarketData,
                    onStockClick = onStockClick,
                    onViewAllClick = { /* Navigate to full list */ }
                )
            }
        }
    }
}
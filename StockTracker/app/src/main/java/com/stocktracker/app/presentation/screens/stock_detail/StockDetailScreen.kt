package com.stocktracker.app.presentation.screens.stock_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stocktracker.app.presentation.components.StockChart
import com.stocktracker.app.presentation.components.WatchlistBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    symbol: String,
    name: String,
    onBackClick: () -> Unit,
    viewModel: StockDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showWatchlistSheet by remember { mutableStateOf(false) }
    
    LaunchedEffect(symbol) {
        viewModel.loadStockData(symbol)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details Screen") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Bookmark action */ }) {
                        Icon(
                            if (state.isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stock Header
            item {
                StockHeaderCard(
                    symbol = symbol,
                    name = name,
                    price = state.currentPrice,
                    change = state.priceChange,
                    changePercent = state.changePercent
                )
            }
            
            // Chart
            item {
                if (state.chartData.isNotEmpty()) {
                    StockChart(
                        data = state.chartData,
                        symbol = symbol
                    )
                }
            }
            
            // Time Period Selector
            item {
                TimePeriodSelector(
                    selectedPeriod = state.selectedPeriod,
                    onPeriodSelected = viewModel::onPeriodSelected
                )
            }
            
            // Add to Watchlist Button
            item {
                Button(
                    onClick = { showWatchlistSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add to Watchlist")
                }
            }
        }
    }
    
    if (showWatchlistSheet) {
        WatchlistBottomSheet(
            onDismiss = { showWatchlistSheet = false },
            onAddToWatchlist = { watchlistName ->
                viewModel.addToWatchlist(symbol, watchlistName)
                showWatchlistSheet = false
            },
            existingWatchlists = state.availableWatchlists
        )
    }
}

@Composable
fun StockHeaderCard(
    symbol: String,
    name: String,
    price: Double,
    change: Double,
    changePercent: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stock Logo Placeholder
                Card(
                    modifier = Modifier.size(48.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = symbol.take(2),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Column {
                    Text(
                        text = symbol,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${"%.2f".format(price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${if (change >= 0) "+" else ""}${"%.2f".format(change)} (${"%.2f".format(changePercent)}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (change >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
fun TimePeriodSelector(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val periods = listOf("1D", "1W", "1M", "3M", "1Y")
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        periods.forEach { period ->
            FilterChip(
                onClick = { onPeriodSelected(period) },
                label = { Text(period) },
                selected = selectedPeriod == period
            )
        }
    }
}
package com.stocktracker.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stocktracker.app.presentation.screens.home.HomeScreen
import com.stocktracker.app.presentation.screens.stock_detail.StockDetailScreen
import com.stocktracker.app.presentation.screens.watchlist.WatchlistScreen

@Composable
fun StockTrackerNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onStockClick = { symbol, name ->
                    navController.navigate("stock_detail/$symbol/$name")
                },
                onWatchlistClick = {
                    navController.navigate("watchlist")
                }
            )
        }
        
        composable("stock_detail/{symbol}/{name}") { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            StockDetailScreen(
                symbol = symbol,
                name = name,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("watchlist") {
            WatchlistScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onStockClick = { symbol, name ->
                    navController.navigate("stock_detail/$symbol/$name")
                }
            )
        }
    }
}
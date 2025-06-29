package com.stocktracker.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == "Home",
            onClick = { onTabSelected("Home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Watchlist") },
            label = { Text("Watchlist") },
            selected = selectedTab == "Watchlist",
            onClick = { onTabSelected("Watchlist") }
        )
    }
}
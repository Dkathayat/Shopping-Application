package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.BottomNavGraph
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.CategoryBottomNav
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf("ProductDetail", "SearchProduct", "Wishlist", "Delivery")
    Scaffold(
        bottomBar = { if (!showBottomBar) { CategoryBottomNav(bottomNavController) } },
        containerColor = BackgroundCream,
    ) { padding ->
        BottomNavGraph(bottomNavController, innerPadding = padding)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    HomeScreen()
}
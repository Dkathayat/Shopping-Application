package com.crystaldairyfarms.crystaldairyfarms.presentation.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.BottomNavGraph
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.CategoryBottomNav
import com.crystaldairyfarms.crystaldairyfarms.ui.theme.BackgroundCream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSignOut: () -> Unit = {}) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val hideBottomBar = currentRoute == "ProductDetail"
        || currentRoute == "Profile"
        || currentRoute == "Checkout"
        || currentRoute?.startsWith("SearchProduct") == true

    Scaffold(
        bottomBar = { if (!hideBottomBar) { CategoryBottomNav(bottomNavController) } },
        containerColor = BackgroundCream,
    ) { padding ->
        BottomNavGraph(
            navController = bottomNavController,
            innerPadding = padding,
            onSignOut = onSignOut
        )
    }
}
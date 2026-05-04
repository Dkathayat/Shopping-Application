package com.crystaldairyfarms.crystaldairyfarms.presentation.appnav

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.crystaldairyfarms.crystaldairyfarms.presentation.currentRoute

data class NavItem(val label: String, val icon: ImageVector, val displayLabel: String = label)
private val CatNavActive   = Color(0xFF1B3F32)
@Composable
fun CategoryBottomNav(
    navController: NavHostController
) {
    val items = listOf(
        NavItem(BottomRoutes.Home,       Icons.Default.Home),
        NavItem(BottomRoutes.Categories, Icons.Default.List),
        NavItem(BottomRoutes.Wishlist,   Icons.Default.FavoriteBorder),
        NavItem(BottomRoutes.Delivery,   Icons.Default.LocalShipping, "Orders"),
    )
    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentRoute == item.label,
                onClick = {
                    // Proper backstack control
                    navController.navigate(item.label) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        //launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(item.displayLabel, fontSize = 10.sp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CatNavActive,
                    selectedTextColor = CatNavActive,
                    unselectedIconColor = Color(0xFFB0B0B0),
                    unselectedTextColor = Color(0xFFB0B0B0),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

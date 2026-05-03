package com.crystaldairyfarms.crystaldairyfarms.presentation.appnav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.CategoryScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.DeliveryScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.HomeContent
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.ProductDetailScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.ProfileScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.SearchProductScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.WishListScreen


@Composable
fun BottomNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomRoutes.Home, // define one as default
    ) {
        composable(BottomRoutes.Home) {
            HomeContent(
                innerPadding,
                {
                    navController.navigate(AppRoutes.SearchProduct)
                }, {
                    navController.navigate(BottomRoutes.Profile)
                }, {
                    navController.navigate(AppRoutes.SearchProduct)
                }, {
                    navController.navigate(AppRoutes.ProductDetail)
                })
        }

        composable(BottomRoutes.Profile) {
            ProfileScreen { }
        }

        composable(BottomRoutes.Categories) {
            CategoryScreen({
                navController.navigate(AppRoutes.SearchProduct)
            })
        }
        composable(BottomRoutes.Wishlist) {
            WishListScreen()
        }
        composable(BottomRoutes.Delivery) {
            DeliveryScreen()
        }
        composable(AppRoutes.ProductDetail) {
            ProductDetailScreen {
                navController.popBackStack()
            }
        }
        composable(AppRoutes.SearchProduct) {
            SearchProductScreen ( onSelect = {
                navController.navigate(AppRoutes.ProductDetail)
            })
        }
    }
}
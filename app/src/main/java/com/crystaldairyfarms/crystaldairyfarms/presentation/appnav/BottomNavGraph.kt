package com.crystaldairyfarms.crystaldairyfarms.presentation.appnav

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.CheckoutScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.SelectedProductViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.toFirebaseProduct

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    // Activity-scoped so both HomeContent and ProductDetailScreen share the same instance.
    val activity = LocalActivity.current as ComponentActivity
    val selectedProductVM: SelectedProductViewModel = viewModel(
        viewModelStoreOwner = activity
    )

    NavHost(
        navController = navController,
        startDestination = BottomRoutes.Home,
    ) {
        composable(BottomRoutes.Home) {
            HomeContent(
                paddingValues = innerPadding,
                onStoreClick = { navController.navigate(AppRoutes.SearchProduct) },
                onDrawerClicked = { navController.navigate(BottomRoutes.Profile) },
                cat = { navController.navigate(AppRoutes.SearchProduct) },
                onItemClick = { navController.navigate(AppRoutes.ProductDetail) },
                onProductClick = { product ->
                    selectedProductVM.select(product)
                    navController.navigate(AppRoutes.ProductDetail)
                },
                onCartItemClick = { item ->
                    selectedProductVM.select(item.toFirebaseProduct())
                    navController.navigate(AppRoutes.ProductDetail)
                },
                onCategoryClick = { homeCategoryName ->
                    val filter = when (homeCategoryName) {
                        "Paneer" -> "Dairy"
                        "Vegi"   -> "Vegetables"
                        "Fruits" -> "Fruits"
                        "Breads" -> "Breads"
                        else     -> homeCategoryName
                    }
                    navController.navigate("${AppRoutes.SearchProduct}?category=$filter")
                },
                onDeliverySlotClick = { label ->
                    val filter = when (label) {
                        "Bread"     -> "Breads"
                        "Dairy"     -> "Dairy"
                        "Vegetable" -> "Vegetables"
                        else        -> "All"
                    }
                    navController.navigate("${AppRoutes.SearchProduct}?category=$filter")
                },
                onCheckout = { navController.navigate(AppRoutes.Checkout) }
            )
        }

        composable(BottomRoutes.Profile) {
            ProfileScreen(
                logOut = {},
                onBack = { navController.popBackStack() }
            )
        }

        composable(BottomRoutes.Categories) {
            CategoryScreen(onCategoryClick = { cat ->
                val filter = when (cat.name) {
                    "Vegetable"     -> "Vegetables"
                    "Fruits"        -> "Fruits"
                    "Breads"        -> "Breads"
                    "Dairy & Sweet" -> "Dairy"
                    "Snacks"        -> "Snacks"
                    "Bakery"        -> "Bakery"
                    "Chicken"       -> "Chicken"
                    else            -> "All"
                }
                navController.navigate("${AppRoutes.SearchProduct}?category=$filter")
            })
        }

        composable(BottomRoutes.Wishlist) {
            WishListScreen(
                bottomPadding = innerPadding.calculateBottomPadding(),
                onProductClick = { product ->
                    selectedProductVM.select(product)
                    navController.navigate(AppRoutes.ProductDetail)
                },
                onCheckout = { navController.navigate(AppRoutes.Checkout) }
            )
        }

        composable(BottomRoutes.Delivery) {
            DeliveryScreen(bottomPadding = innerPadding.calculateBottomPadding())
        }

        composable(AppRoutes.Checkout) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onOrderSuccess = {
                    navController.navigate(BottomRoutes.Delivery) {
                        popUpTo(AppRoutes.Checkout) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.ProductDetail) {
            ProductDetailScreen(
                selectedProductViewModel = selectedProductVM,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${AppRoutes.SearchProduct}?category={category}",
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
                defaultValue = "All"
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "All"
            SearchProductScreen(
                selectedProductViewModel = selectedProductVM,
                initialCategory = category,
                onSelect = { navController.navigate(AppRoutes.ProductDetail) }
            )
        }
    }
}
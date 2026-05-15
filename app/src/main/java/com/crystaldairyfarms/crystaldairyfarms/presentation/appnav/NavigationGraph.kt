package com.crystaldairyfarms.crystaldairyfarms.presentation.appnav

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.MainRoutes.HOME_SCREEN
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.MainRoutes.LOGIN_SCREEN
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.HomeScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.signin.SignInScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.vm.AuthViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    val activity = LocalActivity.current as ComponentActivity
    val authViewModel: AuthViewModel = viewModel(viewModelStoreOwner = activity)

    NavHost(
        navController = navController,
        startDestination = LOGIN_SCREEN
    ) {
        composable(LOGIN_SCREEN) {
            SignInScreen(
                onSignedIn = {
                    navController.navigate(HOME_SCREEN) {
                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onGuestContinue = {
                    navController.navigate(HOME_SCREEN) {
                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(HOME_SCREEN) {
            HomeScreen(
                onSignOut = {
                    navController.navigate(LOGIN_SCREEN) {
                        popUpTo(HOME_SCREEN) { inclusive = true }
                    }
                }
            )
        }
    }
}
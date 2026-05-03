package com.crystaldairyfarms.crystaldairyfarms.presentation.appnav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.MainRoutes.HOME_SCREEN
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.MainRoutes.LOGIN_SCREEN
import com.crystaldairyfarms.crystaldairyfarms.presentation.appnav.MainRoutes.SPLASH_SCREEN
import com.crystaldairyfarms.crystaldairyfarms.presentation.screens.HomeScreen
import com.crystaldairyfarms.crystaldairyfarms.presentation.signin.SignInScreen
import kotlinx.coroutines.delay


@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LOGIN_SCREEN
    ) {
        composable(SPLASH_SCREEN) {
            LaunchedEffect(Unit) {
                delay(2000L)
                navController.navigate(LOGIN_SCREEN) {
                    popUpTo(SPLASH_SCREEN) { inclusive = true }
                }
            }
        }
        composable(LOGIN_SCREEN) {
            Log.d("NAV", "➡️ Composing LOGIN_SCREEN")
            SignInScreen(
                onGoogleSignInClick = {
                    Log.d("NAV", "✅ onGoogleSignInClick fired, navigating to HOME")
                    navController.navigate(HOME_SCREEN) {
                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onEmailSignInClick = {
                    Log.d("NAV", "✅ onEmailSignInClick fired")
                }
            )
        }

        composable(HOME_SCREEN) {
            Log.d("NAV", "➡️ Composing HOME_SCREEN")
            HomeScreen()
        }

    }
}
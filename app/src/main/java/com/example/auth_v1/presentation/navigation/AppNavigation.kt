package com.example.auth_v1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.auth_v1.presentation.auth.AuthState
import com.example.auth_v1.presentation.auth.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val isUserLoggedIn = authViewModel.isUserLoggedIn

    // Determine start destination based on auth state
    val startDestination = if (isUserLoggedIn) {
        NavigationDestinations.MAIN_GRAPH
    } else {
        NavigationDestinations.AUTH_GRAPH
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // User successfully authenticated, navigate to main
                navController.navigate(NavigationDestinations.MAIN_GRAPH) {
                    popUpTo(NavigationDestinations.AUTH_GRAPH) {
                        inclusive = true
                    }
                }
            }

            is AuthState.LoggedOut -> {
                // User logged out, navigate to auth
                navController.navigate(NavigationDestinations.AUTH_GRAPH) {
                    popUpTo(NavigationDestinations.MAIN_GRAPH) {
                        inclusive = true
                    }
                }
            }

            else -> {
                // Other states (Initial, Loading, Error) - stay on current screen
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authNavigation(
            navController = navController,
            onNavigateToMain = {
                navController.navigate(NavigationDestinations.MAIN_GRAPH) {
                    popUpTo(NavigationDestinations.AUTH_GRAPH) {
                        inclusive = true
                    }
                }
            }
        )
        mainNavigation(
            navController = navController,
            onNavigateToAuth = {
                authViewModel.logout()
                navController.navigate(NavigationDestinations.AUTH_GRAPH) {
                    popUpTo(NavigationDestinations.MAIN_GRAPH) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
package com.example.auth_v1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.auth_v1.presentation.main.MainScreen


fun NavGraphBuilder.mainNavigation(
    navController: NavHostController,
    onNavigateToAuth: () -> Unit
) {

    navigation(
        startDestination = NavigationDestinations.HOME,
        route = NavigationDestinations.MAIN_GRAPH
    ) {

        // Main Screen (wrapper for bottom navigation)
        composable(NavigationDestinations.HOME) {
            MainScreen(
                onLogout = {
                    onNavigateToAuth()
                }
            )
        }
}}
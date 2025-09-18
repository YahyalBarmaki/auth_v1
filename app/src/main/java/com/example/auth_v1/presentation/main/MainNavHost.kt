package com.example.auth_v1.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth_v1.presentation.home.HomeScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        // Home Screen
        composable("home") {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    // Navigate to user profile
                },
                onNavigateToChat = { chatId ->
                    // Navigate to specific chat
                }
            )
        }


}}
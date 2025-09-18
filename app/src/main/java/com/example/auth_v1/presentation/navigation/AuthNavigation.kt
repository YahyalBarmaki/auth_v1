package com.example.auth_v1.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.auth_v1.presentation.auth.AuthState
import com.example.auth_v1.presentation.auth.LoginScreen
import com.example.auth_v1.presentation.auth.RegisterScreen
import com.example.auth_v1.presentation.auth.AuthViewModel

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    onNavigateToMain: () -> Unit
) {
    navigation(
        startDestination = NavigationDestinations.LOGIN,
        route = NavigationDestinations.AUTH_GRAPH
    ) {
        // Login Screen
        composable(NavigationDestinations.LOGIN) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState()
            val loginFormState by authViewModel.loginFormState.collectAsState()

            when (authState) {

                is AuthState.Success -> {
                    onNavigateToMain()
                    authViewModel.clearAuthState()
                }
                else -> {
                    // Stay on login screen
                }
            }
            LoginScreen(
                onBackPressed = {
                    // Handle back press - maybe exit app or go to welcome screen
                    navController.popBackStack()
                },
                onSignInPressed = { email, password ->
                    authViewModel.updateLoginEmail(email)
                    authViewModel.updateLoginPassword(password)
                    authViewModel.login()
                },
                onGoogleSignIn = {
                    authViewModel.signInWithGoogle()
                },
                onAppleSignIn = {
                    authViewModel.signInWithApple()
                },
                onRegisterPressed = {
                    navController.navigate(NavigationDestinations.REGISTER)
                },
                onSupportPressed = {
                    // Navigate to support or open external link
                    // Could be external link or in-app support
                },
                onForgotPasswordPressed = {
                    navController.navigate(NavigationDestinations.FORGOT_PASSWORD)
                }
            )

        }

        // Register Screen
        composable(NavigationDestinations.REGISTER) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState by authViewModel.authState.collectAsState()
            val registerFormState by authViewModel.registerFormState.collectAsState()

            // Navigate to main when registration successful
            when (authState) {
                is com.example.auth_v1.presentation.auth.AuthState.Success -> {
                    onNavigateToMain()
                    authViewModel.clearAuthState()
                }
                else -> {
                    // Stay on register screen
                }
            }

            RegisterScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onRegisterPressed = { fullName, email, password, confirmPassword ->
                    authViewModel.updateRegisterFullName(fullName)
                    authViewModel.updateRegisterEmail(email)
                    authViewModel.updateRegisterPassword(password)
                    authViewModel.updateRegisterConfirmPassword(confirmPassword)
                    authViewModel.register()
                },
                onGoogleSignUp = {
                    authViewModel.signInWithGoogle()
                },
                onAppleSignUp = {
                    authViewModel.signInWithApple()
                },
                onLoginPressed = {
                    navController.popBackStack() // Go back to login
                },
                onSupportPressed = {
                    // Navigate to support
                }
            )
        }


        // Forgot Password Screen (placeholder)
        composable(NavigationDestinations.FORGOT_PASSWORD) {
            val authViewModel: AuthViewModel = hiltViewModel()

            // TODO: Implement ForgotPasswordScreen
            // ForgotPasswordScreen(
            //     onBackPressed = { navController.popBackStack() },
            //     onResetPressed = { email -> authViewModel.forgotPassword(email) }
            // )
        }

        // Email Verification Screen (placeholder)
        composable(
            route = NavigationDestinations.EMAIL_VERIFICATION_WITH_EMAIL,
            arguments = listOf(
                navArgument(NavigationArgs.EMAIL) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(NavigationArgs.EMAIL) ?: ""
            val authViewModel: AuthViewModel = hiltViewModel()

            // TODO: Implement EmailVerificationScreen
            // EmailVerificationScreen(
            //     email = email,
            //     onBackPressed = { navController.popBackStack() },
            //     onVerifyPressed = { code -> authViewModel.verifyEmail(code) }
            // )
        }






        }











}
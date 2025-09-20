package com.example.auth_v1.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth_v1.Constants.LetsConnectColors
import com.example.auth_v1.presentation.auth.common.*

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onSupportPressed: () -> Unit = {},
    onForgotPasswordPressed: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val loginFormState by viewModel.loginFormState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onNavigateToHome()
            }
            else -> { /* Handle other states */ }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LetsConnectColors.Background,
                        Color(0xFF1A1A1A)
                    )
                )
            )
    ) {
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = LetsConnectColors.OnSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoSection()

            Spacer(modifier = Modifier.height(48.dp))

           // SupportLink(onClick = onSupportPressed)

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            CustomTextField(
                value = loginFormState.email,
                onValueChange = viewModel::updateLoginEmail,
                label = "Email",
                isError = loginFormState.emailError != null,
                errorMessage = loginFormState.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            PasswordTextField(
                value = loginFormState.password,
                onValueChange = viewModel::updateLoginPassword,
                label = "Password",
                isError = loginFormState.passwordError != null,
                errorMessage = loginFormState.passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sign In button
            SignInButton(
                onClick = { viewModel.login() },
                isLoading = loginFormState.isLoading,
                enabled = loginFormState.isValid && !loginFormState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            // Error message
            if (authState is AuthState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password link
            ForgotPasswordLink(onClick = onForgotPasswordPressed)

            Spacer(modifier = Modifier.height(24.dp))

            // Social sign-in buttons
            SocialSignInButtons(
                onGoogleClick = viewModel::signInWithGoogle,
                onAppleClick = viewModel::signInWithApple
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register link
            RegisterLink(onClick = onNavigateToRegister)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(
        modifier = Modifier,
        onBackPressed = {},
        onNavigateToRegister = {},
        onNavigateToHome = {},
        onSupportPressed = {},
        onForgotPasswordPressed = {}
    )
}
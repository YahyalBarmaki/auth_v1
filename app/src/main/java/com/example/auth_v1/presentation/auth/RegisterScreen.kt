package com.example.auth_v1.presentation.auth

import androidx.compose.foundation.clickable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import com.example.auth_v1.Constants.LetsConnectColors
import com.example.auth_v1.presentation.auth.common.CustomTextField
import com.example.auth_v1.presentation.auth.common.LogoSection
import com.example.auth_v1.presentation.auth.common.PasswordTextField
import com.example.auth_v1.presentation.auth.common.SignInButton
import com.example.auth_v1.presentation.auth.common.SocialSignInButtons
import com.example.auth_v1.presentation.auth.common.SupportLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackPressed: () -> Unit = {},
    onRegisterPressed: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onGoogleSignUp: () -> Unit = {},
    onAppleSignUp: () -> Unit = {},
    onLoginPressed: () -> Unit = {},
    onSupportPressed: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val registerFormState by viewModel.registerFormState.collectAsState()

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
            Spacer(modifier = Modifier.height(24.dp))
            
            LogoSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Registration header text
            Text(
                text = "Create Your Account",
                color = LetsConnectColors.OnSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Join us and start connecting",
                color = LetsConnectColors.OnSurfaceVariant,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Support link
          //  SupportLink(onClick = onSupportPressed)

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name input
            CustomTextField(
                value = registerFormState.fullName,
                onValueChange = viewModel::updateRegisterFullName,
                label = "Enter Full Name",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
                isError = registerFormState.fullNameError != null,
                errorMessage = registerFormState.fullNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email input
            CustomTextField(
                value = registerFormState.email,
                onValueChange = viewModel::updateRegisterEmail,
                label = "Enter Email Address",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                isError = registerFormState.emailError != null,
                errorMessage = registerFormState.emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            PasswordTextField(
                value = registerFormState.password,
                onValueChange = viewModel::updateRegisterPassword,
                label = "Enter Password",
                modifier = Modifier.fillMaxWidth(),
                isError = registerFormState.passwordError != null,
                errorMessage = registerFormState.passwordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password input
            PasswordTextField(
                value = registerFormState.confirmPassword,
                onValueChange = viewModel::updateRegisterConfirmPassword,
                label = "Confirm Password",
                modifier = Modifier.fillMaxWidth(),
                isError = registerFormState.confirmPasswordError != null,
                errorMessage = registerFormState.confirmPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register button
            SignInButton(
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth(),
                enabled = registerFormState.isValid && !registerFormState.isLoading,
                isLoading = registerFormState.isLoading,
                text = "Create Account"
            )

            // Error message from AuthState
            if (authState is AuthState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(LetsConnectColors.Outline)
                )
                Text(
                    text = "Or",
                    color = LetsConnectColors.OnSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(LetsConnectColors.Outline)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social sign-up buttons
            SocialSignInButtons(
                onGoogleClick = onGoogleSignUp,
                onAppleClick = onAppleSignUp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = LetsConnectColors.OnSurfaceVariant,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = LetsConnectColors.Primary2,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onLoginPressed() }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    RegisterScreen(
        onBackPressed = {},
        onRegisterPressed = { _, _, _, _ -> },
        onGoogleSignUp = {},
        onAppleSignUp = {},
        onLoginPressed = {},
        onSupportPressed = {}
    )
}
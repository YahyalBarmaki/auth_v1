package com.example.auth_v1.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth_v1.Constants.LetsConnectColors
import com.example.auth_v1.presentation.auth.common.CustomTextField
import com.example.auth_v1.presentation.auth.common.LogoSection
import com.example.auth_v1.presentation.auth.common.PasswordTextField
import com.example.auth_v1.presentation.auth.common.RegisterLink
import com.example.auth_v1.presentation.auth.common.SignInButton
import com.example.auth_v1.presentation.auth.common.SocialSignInButtons
import com.example.auth_v1.presentation.auth.common.SupportLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackPressed: () -> Unit = {},
    onSignInPressed: (String, String) -> Unit = { _, _ -> },
    onGoogleSignIn: () -> Unit = {},
    onAppleSignIn: () -> Unit = {},
    onRegisterPressed: () -> Unit = {},
    onSupportPressed: () -> Unit = {},
    onForgotPasswordPressed: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
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

                // Support link
               // SupportLink(onClick = onSupportPressed)

                Spacer(modifier = Modifier.height(32.dp))

                // Username/Email input
            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Enter Username Or Email",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )

                Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                modifier = Modifier.fillMaxWidth(),
                isVisible = isPasswordVisible,
                onVisibilityToggle = { isPasswordVisible = !isPasswordVisible }
            )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Recovery Password",
                    color = LetsConnectColors.OnSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable { onForgotPasswordPressed() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Sign In button
            SignInButton(
                onClick = { onSignInPressed(username, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotBlank() && password.isNotBlank(),
                text = "Sign In"
            )
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

                // Social sign-in buttons
            SocialSignInButtons(
                onGoogleClick = onGoogleSignIn,
                onAppleClick = onAppleSignIn
            )

                Spacer(modifier = Modifier.height(32.dp))

                // Register link
            RegisterLink(onClick = onRegisterPressed)
            }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier)
}
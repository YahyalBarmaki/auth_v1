package com.example.auth_v1.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.auth_v1.Constants.LetsConnectColors

@Composable
fun HomeScreen(
    onNavigateToProfile: (String) -> Unit = {},
    onNavigateToChat: (String) -> Unit = {},
    onClearSession: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "Home Screen\n\nWelcome to LetsConnect!",
                style = MaterialTheme.typography.headlineSmall,
                color = LetsConnectColors.OnSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))
            
            // Debug button to clear session
            androidx.compose.material3.Button(
                onClick = onClearSession,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = LetsConnectColors.Error
                )
            ) {
                Text("Clear Session (Debug)", color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}
package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth_v1.Constants.LetsConnectColors

@Composable
fun LogoSection() {
    // Custom logo design for "LET'S CONNECT"
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(
                LetsConnectColors.Primary2,
                RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LC",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "LET'S CONNECT",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = LetsConnectColors.OnSurface,
        letterSpacing = 1.5.sp
    )
}

@Composable
@Preview(showBackground = true)
fun LogoSectionPreview() {
    LogoSection()
}

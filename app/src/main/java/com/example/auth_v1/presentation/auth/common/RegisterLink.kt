package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.auth_v1.Constants.LetsConnectColors
import com.example.auth_v1.Constants.LetsConnectColors.Primary2

@Composable
public fun RegisterLink(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                println("RegisterLink clicked!")
                onClick() 
            }
    ) {
        Text(
            text = "Not A Member ? ",
            color = LetsConnectColors.OnSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = "Register Now",
            color = Color(LetsConnectColors.Primary2.value),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterLinkPreview() {
    RegisterLink(onClick = {})
}
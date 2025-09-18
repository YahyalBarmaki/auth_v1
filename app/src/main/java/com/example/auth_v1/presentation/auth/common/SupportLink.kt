package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.auth_v1.Constants.LetsConnectColors

@Composable
 fun SupportLink(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "If You Need Any Support ",
            color = LetsConnectColors.OnSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = "Click Here",
            color = LetsConnectColors.Primary2,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SupportLinkPreview() {
    SupportLink(onClick = {})
}
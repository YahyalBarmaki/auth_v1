package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth_v1.R

@Composable
 fun SocialSignInButtons(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(44.dp),
        modifier = Modifier
            .padding(start = 94.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = onGoogleClick,
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(28.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Sign In",
                modifier = Modifier.size(24.dp)
            )
        }
        
        IconButton(
            onClick = onAppleClick,
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(28.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.apple),
                contentDescription = "Apple Sign In",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SocialSignInButtonsPreview() {
    SocialSignInButtons(
        onGoogleClick = {},
        onAppleClick = {}
    )
}

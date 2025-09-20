package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth_v1.Constants.LetsConnectColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = LetsConnectColors.OnSurfaceVariant
            )
        },
        modifier = modifier,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) LetsConnectColors.Error else LetsConnectColors.Primary2,
            unfocusedBorderColor = if (isError) LetsConnectColors.Error else LetsConnectColors.Outline,
            focusedTextColor = androidx.compose.ui.graphics.Color.White,
            unfocusedTextColor = androidx.compose.ui.graphics.Color.White,
            cursorColor = LetsConnectColors.Primary2
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        supportingText = if (isError && errorMessage != null) {
            {
                Text(
                    text = errorMessage,
                    color = LetsConnectColors.Error
                )
            }
        } else null
    )
}

@Composable
@Preview(showBackground = true)
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "",
        onValueChange = {},
        label = "Placeholder"
    )
}
package com.example.auth_v1.presentation.auth.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.auth_v1.Constants.LetsConnectColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    isVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
){
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
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (isVisible) "Hide password" else "Show password",
                    tint = LetsConnectColors.OnSurfaceVariant
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) LetsConnectColors.Error else LetsConnectColors.Primary2,
            unfocusedBorderColor = if (isError) LetsConnectColors.Error else LetsConnectColors.Outline,
            focusedTextColor = LetsConnectColors.OnSurface,
            unfocusedTextColor = LetsConnectColors.OnSurface,
            cursorColor = LetsConnectColors.Primary2
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
fun PasswordTextFieldPreview(){
    PasswordTextField(
        value = "",
        onValueChange = {},
        label = "Password",
        isVisible = true,
        onVisibilityToggle = {}
    )
}
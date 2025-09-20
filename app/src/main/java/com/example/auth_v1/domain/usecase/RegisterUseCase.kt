package com.example.auth_v1.domain.usecase

import android.util.Log
import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
)  {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
    ):Result<AuthResponse> {
        Log.d("RegisterUseCase", "Starting registration validation")
        Log.d("RegisterUseCase", "Name: $name, Email: $email, Password length: ${password.length}")

        if (name.isBlank()) {
            Log.e("RegisterUseCase", "Name is blank")
            return Result.Error(
                exception = IllegalArgumentException("Name cannot be empty"),
                message = "Name is required"
            )
        }
        if (name.length < 2) {
            Log.e("RegisterUseCase", "Name too short: ${name.length}")
            return Result.Error(
                exception = IllegalArgumentException("Name too short"),
                message = "Name must be at least 2 characters"
            )
        }

        if (email.isBlank()) {
            Log.e("RegisterUseCase", "Email is blank")
            return Result.Error(
                exception = IllegalArgumentException("Email cannot be empty"),
                message = "Email is required"
            )
        }

        if (!isValidEmail(email)) {
            Log.e("RegisterUseCase", "Invalid email format: $email")
            return Result.Error(
                exception = IllegalArgumentException("Invalid email format"),
                message = "Please enter a valid email address"
            )
        }

        if (password.isBlank()) {
            Log.e("RegisterUseCase", "Password is blank")
            return Result.Error(
                exception = IllegalArgumentException("Password cannot be empty"),
                message = "Password is required"
            )
        }

        if (password.length < 8) {
            Log.e("RegisterUseCase", "Password too short: ${password.length} characters")
            return Result.Error(
                exception = IllegalArgumentException("Password too short"),
                message = "Password must be at least 8 characters"
            )
        }

        if (!isStrongPassword(password)) {
            Log.e("RegisterUseCase", "Password not strong enough")
            return Result.Error(
                exception = IllegalArgumentException("Weak password"),
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one number"
            )
        }

        if (password != confirmPassword) {
            Log.e("RegisterUseCase", "Passwords don't match")
            return Result.Error(
                exception = IllegalArgumentException("Passwords don't match"),
                message = "Passwords do not match"
            )
        }

        Log.d("RegisterUseCase", "All validations passed, calling authRepository.register")
        return try {
            authRepository.register(
                email = email.trim().lowercase(),
                password = password,
                name = name.trim()
            )
        } catch (e: Exception) {
            Log.e("RegisterUseCase", "Exception in register: ${e.message}")
            Result.Error(
                exception = e,
                message = "Registration failed. Please try again."
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isStrongPassword(password: String): Boolean {
        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasUppercase && hasLowercase && hasDigit
    }
}
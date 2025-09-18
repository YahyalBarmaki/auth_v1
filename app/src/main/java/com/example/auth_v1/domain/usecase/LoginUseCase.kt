package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result

import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
)  {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse>
    {

        if (email.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Email cannot be empty"),
                message = "Email is required"
            )
        }

        if (!isValidEmail(email)) {
            return Result.Error(
                exception = IllegalArgumentException("Invalid email format"),
                message = "Please enter a valid email address"
            )
        }


        if (password.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Password cannot be empty"),
                message = "Password is required"
            )
        }

        if (password.length < 6) {
            return Result.Error(
                exception = IllegalArgumentException("Password too short"),
                message = "Password must be at least 6 characters"
            )
        }

        return try {
            authRepository.login(email.trim().lowercase(), password)
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Login failed. Please try again."
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
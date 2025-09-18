package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
){

    suspend operator fun invoke(email: String): Result<Unit> {

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

        return try {
            authRepository.forgotPassword(email.trim().lowercase())
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Failed to send reset email. Please try again."
            )
        }

    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
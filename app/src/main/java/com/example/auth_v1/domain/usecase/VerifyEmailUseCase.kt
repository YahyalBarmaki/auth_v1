package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
){

    suspend operator fun invoke(verificationCode: String): Result<Unit> {
        if (verificationCode.isBlank()) {
            return Result.Error(
                exception = IllegalArgumentException("Verification code cannot be empty"),
                message = "Verification code is required"
            )
        }

        if (verificationCode.length != 6) {
            return Result.Error(
                exception = IllegalArgumentException("Invalid verification code"),
                message = "Verification code must be 6 digits"
            )
        }

        if (!verificationCode.all { it.isDigit() }) {
            return Result.Error(
                exception = IllegalArgumentException("Invalid verification code format"),
                message = "Verification code must contain only numbers"
            )
        }

        return try {
            authRepository.verifyEmail(verificationCode.trim())
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Email verification failed. Please try again."
            )
        }
    }
}
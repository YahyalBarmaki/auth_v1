package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
){

    suspend operator fun invoke(): Result<AuthResponse> {
        return try {
            authRepository.refreshToken()
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Session expired. Please log in again."
            )
        }
    }

}
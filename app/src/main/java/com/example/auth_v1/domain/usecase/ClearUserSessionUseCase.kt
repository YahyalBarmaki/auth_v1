package com.example.auth_v1.domain.usecase
import com.example.auth_v1.domain.repositories.AuthRepository
import javax.inject.Inject

class ClearUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.clearUserSession()
    }
}
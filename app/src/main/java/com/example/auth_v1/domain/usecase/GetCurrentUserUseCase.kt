package com.example.auth_v1.domain.usecase


import com.example.auth_v1.domain.model.User
import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun getCurrentUser(): Result<User> {
        return try {
            if (!authRepository.isUserLoggedIn()) {
                Result.Error(
                    exception = IllegalStateException("User not logged in"),
                    message = "Please log in to continue"
                )
            } else {
                authRepository.getCurrentUser()
            }
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Failed to get user information"
            )
        }
    }

    fun getUserFlow(): Flow<User?> {
        return authRepository.getUserFlow()
    }

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
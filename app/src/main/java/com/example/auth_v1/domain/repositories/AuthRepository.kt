package com.example.auth_v1.domain.repositories

import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.model.User
import kotlinx.coroutines.flow.Flow

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, password: String, name: String): Result<AuthResponse>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<User>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun verifyEmail(verificationCode: String): Result<Unit>

    fun isUserLoggedIn(): Boolean
    fun getUserFlow(): Flow<User?>
    fun getAccessToken(): String?
    suspend fun clearUserSession()
}
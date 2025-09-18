package com.example.auth_v1.data.repository

import android.util.Log
import com.example.auth_v1.data.local.TokenStorage
import com.example.auth_v1.data.local.UserPreferences
import com.example.auth_v1.data.remote.dto.AuthApiService
import com.example.auth_v1.data.remote.dto.LoginRequestDto
import com.example.auth_v1.data.remote.dto.RefreshTokenRequestDto
import com.example.auth_v1.data.remote.dto.RegisterRequestDto
import com.example.auth_v1.data.remote.dto.toDomain
import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.model.User
import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val tokenStorage: TokenStorage,
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val request = LoginRequestDto(email, password)
            val response = apiService.login(request)

            if (response.isSuccessful) {
                response.body()?.let { simpleAuthResponseDto ->
                    Log.d("AuthRepository", "Token reçu: ${simpleAuthResponseDto.token}")
                    val authResponse = simpleAuthResponseDto.toDomain()
                    Log.d("AuthRepository", "User décodé: ${authResponse.user}")
                    Log.d("AuthRepository", "AccessToken: ${authResponse.accessToken}")
                    saveAuthData(authResponse)
                    Result.Success(authResponse)
                } ?: Result.Error(Exception("Empty response body"))
            } else {
                Log.e("AuthRepository", "Login failed: ${response.code()} - ${response.message()}")
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Result<AuthResponse> {
        return try {
            val request = RegisterRequestDto(email, password, name)
            val response = apiService.register(request)

            if (response.isSuccessful) {
                response.body()?.let { authResponseDto ->
                    val authResponse = authResponseDto.toDomain()
                    saveAuthData(authResponse)
                    Result.Success(authResponse)
                } ?: Result.Error(Exception("Empty response body"))
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val token = tokenStorage.getAccessToken()
            if (token != null) {
                val response = apiService.logout("Bearer $token")
                if (response.isSuccessful) {
                    clearUserSession()
                    Result.Success(Unit)
                } else {
                    // Even if logout fails on server, clear local session
                    clearUserSession()
                    Result.Success(Unit)
                }
            } else {
                clearUserSession()
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            // Clear local session even if network call fails
            clearUserSession()
            Result.Success(Unit)
        }
    }

    override suspend fun refreshToken(): Result<AuthResponse> {
        return try {
            val refreshToken = tokenStorage.getRefreshToken()
            if (refreshToken == null) {
                return Result.Error(Exception("No refresh token available"))
            }

            val request = RefreshTokenRequestDto(refreshToken)
            val response = apiService.refreshToken(request)

            if (response.isSuccessful) {
                response.body()?.let { authResponseDto ->
                    val authResponse = authResponseDto.toDomain()
                    saveAuthData(authResponse)
                    Result.Success(authResponse)
                } ?: Result.Error(Exception("Empty response body"))
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val token = tokenStorage.getAccessToken()
            if (token == null) {
                Log.e("AuthRepository", "No access token available")
                return Result.Error(Exception("No access token available"))
            }

            Log.d("AuthRepository", "Calling /auth/me with token: Bearer $token")
            val response = apiService.getCurrentUser("Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { userDto ->
                    Log.d("AuthRepository", "User reçu: $userDto")
                    val user = userDto.toDomain()
                    Log.d("AuthRepository", "User converti: $user")
                    userPreferences.saveUser(user)
                    Result.Success(user)
                } ?: {
                    Log.e("AuthRepository", "Empty response body from /auth/me")
                    Result.Error(Exception("Empty response body"))
                }()
            } else {
                Log.e("AuthRepository", "getCurrentUser failed: ${response.code()} - ${response.message()}")
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Log.e("AuthRepository", "Network error in getCurrentUser: ${e.message}")
            Result.Error(e)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error in getCurrentUser: ${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = apiService.forgotPassword(mapOf("email" to email))

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun verifyEmail(verificationCode: String): Result<Unit> {
        return try {
            val response = apiService.verifyEmail(mapOf("code" to verificationCode))

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return tokenStorage.hasValidToken()
    }

    override fun getUserFlow(): Flow<User?> {
        return userPreferences.userFlow
    }

    override fun getAccessToken(): String? {
        return tokenStorage.getAccessToken()
    }

    override suspend fun clearUserSession() {
        tokenStorage.clearTokens()
        userPreferences.clearUser()
    }

    private suspend fun saveAuthData(authResponse: AuthResponse) {
        // Save tokens
        tokenStorage.saveAccessToken(authResponse.accessToken)
        tokenStorage.saveRefreshToken(authResponse.refreshToken)
        tokenStorage.saveTokenExpiration(
            System.currentTimeMillis() + (authResponse.expiresIn * 1000)
        )

        // Save user data
        userPreferences.saveUser(authResponse.user)
    }

}
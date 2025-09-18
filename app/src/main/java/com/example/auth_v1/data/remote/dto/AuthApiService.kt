package com.example.auth_v1.data.remote.dto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequestDto): Response<SimpleAuthResponseDto>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequestDto): Response<AuthResponseDto>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshTokenRequestDto): Response<AuthResponseDto>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @GET("auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserDto>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<Unit>

    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body verificationCode: Map<String, String>): Response<Unit>


}
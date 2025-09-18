package com.example.auth_v1.data.remote.dto

import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

fun AuthResponseDto.toDomain(): AuthResponse = AuthResponse(
    user = user.toDomain(),
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn
)

fun UserDto.toDomain(): User = User(
    id = id.toString(), // Conversion Int -> String
    email = email,
    name = name,
    profilePictureUrl = profilePictureUrl,
    isEmailVerified = isEmailVerified,
    createdAt = createdAt
)
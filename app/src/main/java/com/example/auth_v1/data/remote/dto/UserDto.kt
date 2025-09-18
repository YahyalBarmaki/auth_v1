package com.example.auth_v1.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int, // Changé en Int pour correspondre à votre backend
    val email: String,
    val name: String,
    val profilePictureUrl: String? = null,
    val isEmailVerified: Boolean = true, // Par défaut true
    val createdAt: String? = null
)
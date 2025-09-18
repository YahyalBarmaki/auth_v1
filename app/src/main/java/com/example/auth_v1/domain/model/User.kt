package com.example.auth_v1.domain.model

data class User(
val id: String,
val email: String,
val name: String,
val profilePictureUrl: String? = null,
val isEmailVerified: Boolean = false,
val createdAt: String? = null
)
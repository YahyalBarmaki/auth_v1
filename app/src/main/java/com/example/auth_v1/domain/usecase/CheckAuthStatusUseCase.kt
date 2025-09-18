package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.repositories.AuthRepository

import javax.inject.Inject

class CheckAuthStatusUseCase  @Inject constructor(
    private val authRepository: AuthRepository
)
{
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun getAccessToken(): String? {
        return authRepository.getAccessToken()
    }
}
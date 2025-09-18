package com.example.auth_v1.domain.usecase

import com.example.auth_v1.domain.repositories.AuthRepository
import com.example.auth_v1.domain.repositories.Result
import javax.inject.Inject

class LogoutUseCase@Inject constructor(
    private val authRepository: AuthRepository
)  {
    suspend operator fun invoke(): Result<Unit> {

        return try{
            authRepository.logout()
        }catch (e:Exception){
            Result.Error(
                exception = e,
                message = "Logout failed. Please try again."
            )
        }
    }
}
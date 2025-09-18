package com.example.auth_v1.presentation.auth

import com.example.auth_v1.domain.model.User

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
}

data class RegisterFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = fullName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                fullNameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
}

sealed class AuthAction {
    object ForgotPassword : AuthAction()
    object EmailVerification : AuthAction()
    object SocialSignIn : AuthAction()
}

data class AuthActionState(
    val action: AuthAction? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false
)
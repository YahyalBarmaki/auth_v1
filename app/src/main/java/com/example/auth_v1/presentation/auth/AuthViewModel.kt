package com.example.auth_v1.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth_v1.domain.model.User
import com.example.auth_v1.domain.usecase.CheckAuthStatusUseCase
import com.example.auth_v1.domain.usecase.ClearUserSessionUseCase
import com.example.auth_v1.domain.usecase.ForgotPasswordUseCase
import com.example.auth_v1.domain.usecase.GetCurrentUserUseCase
import com.example.auth_v1.domain.usecase.LoginUseCase
import com.example.auth_v1.domain.usecase.LogoutUseCase
import com.example.auth_v1.domain.usecase.RegisterUseCase
import com.example.auth_v1.domain.usecase.VerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import com.example.auth_v1.domain.repositories.Result
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    private val clearUserSessionUseCase: ClearUserSessionUseCase
) : ViewModel() {

    // Main auth state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Login form state
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState: StateFlow<LoginFormState> = _loginFormState.asStateFlow()

    // Register form state
    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState: StateFlow<RegisterFormState> = _registerFormState.asStateFlow()

    // Auth actions state (forgot password, email verification, etc.)
    private val _authActionState = MutableStateFlow(AuthActionState())
    val authActionState: StateFlow<AuthActionState> = _authActionState.asStateFlow()

    // User flow from domain
    val userFlow: StateFlow<User?> = getCurrentUserUseCase.getUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    val isUserLoggedIn: Boolean
        get() = checkAuthStatusUseCase()

    init {
        checkAuthStatus()
    }

    // ============================================
    // LOGIN FUNCTIONS
    // ============================================

    fun updateLoginEmail(email: String) {
        _loginFormState.value = _loginFormState.value.copy(
            email = email,
            emailError = validateEmail(email)
        )
    }

    fun updateLoginPassword(password: String) {
        _loginFormState.value = _loginFormState.value.copy(
            password = password,
            passwordError = validatePassword(password)
        )
    }

    fun login() {
        val currentState = _loginFormState.value

        if (!currentState.isValid) {
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _loginFormState.value = currentState.copy(isLoading = true)

            when (val result = loginUseCase(currentState.email, currentState.password)) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data.user)
                    _loginFormState.value = currentState.copy(isLoading = false)
                }

                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message ?: "Login failed")
                    _loginFormState.value = currentState.copy(isLoading = false)
                }

                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    // ============================================
    // REGISTER FUNCTIONS
    // ============================================

    fun updateRegisterFullName(fullName: String) {
        _registerFormState.value = _registerFormState.value.copy(
            fullName = fullName,
            fullNameError = validateFullName(fullName)
        )
    }

    fun updateRegisterEmail(email: String) {
        _registerFormState.value = _registerFormState.value.copy(
            email = email,
            emailError = validateEmail(email)
        )
    }

    fun updateRegisterPassword(password: String) {
        _registerFormState.value = _registerFormState.value.copy(
            password = password,
            passwordError = validatePassword(password),
            confirmPasswordError = if (_registerFormState.value.confirmPassword.isNotBlank()) {
                validateConfirmPassword(password, _registerFormState.value.confirmPassword)
            } else null
        )
    }

    fun updateRegisterConfirmPassword(confirmPassword: String) {
        _registerFormState.value = _registerFormState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = validateConfirmPassword(
                _registerFormState.value.password,
                confirmPassword
            )
        )
    }

    fun register() {
        val currentState = _registerFormState.value

        if (!currentState.isValid) {
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _registerFormState.value = currentState.copy(isLoading = true)

            when (val result = registerUseCase(
                email = currentState.email,
                password = currentState.password,
                confirmPassword = currentState.confirmPassword,
                name = currentState.fullName
            )) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data.user)
                    _registerFormState.value = currentState.copy(isLoading = false)
                }

                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message ?: "Registration failed")
                    _registerFormState.value = currentState.copy(isLoading = false)
                }

                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    // ============================================
    // OTHER AUTH FUNCTIONS
    // ============================================

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (logoutUseCase()) {
                is Result.Success -> {
                    _authState.value = AuthState.LoggedOut
                    clearFormStates()
                }

                is Result.Error -> {
                    // Even if logout fails on server, clear local state
                    _authState.value = AuthState.LoggedOut
                    clearFormStates()
                }

                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _authActionState.value = AuthActionState(
                action = AuthAction.ForgotPassword,
                isLoading = true
            )

            when (val result = forgotPasswordUseCase(email)) {
                is Result.Success -> {
                    _authActionState.value = AuthActionState(
                        action = AuthAction.ForgotPassword,
                        isLoading = false,
                        isSuccess = true,
                        message = "Password reset email sent successfully"
                    )
                }

                is Result.Error -> {
                    _authActionState.value = AuthActionState(
                        action = AuthAction.ForgotPassword,
                        isLoading = false,
                        isSuccess = false,
                        message = result.message ?: "Failed to send reset email"
                    )
                }

                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun verifyEmail(verificationCode: String) {
        viewModelScope.launch {
            _authActionState.value = AuthActionState(
                action = AuthAction.EmailVerification,
                isLoading = true
            )

            when (val result = verifyEmailUseCase(verificationCode)) {
                is Result.Success -> {
                    _authActionState.value = AuthActionState(
                        action = AuthAction.EmailVerification,
                        isLoading = false,
                        isSuccess = true,
                        message = "Email verified successfully"
                    )
                    // Refresh user data
                    getCurrentUser()
                }

                is Result.Error -> {
                    _authActionState.value = AuthActionState(
                        action = AuthAction.EmailVerification,
                        isLoading = false,
                        isSuccess = false,
                        message = result.message ?: "Email verification failed"
                    )
                }

                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase.getCurrentUser()) {
                is Result.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }

                is Result.Error -> {
                    _authState.value = AuthState.Error(result.message ?: "Failed to get user")
                }

                is Result.Loading -> {
                    _authState.value = AuthState.Loading
                }
            }
        }
    }
    // ============================================
    // SOCIAL SIGN IN (placeholder for future implementation)
    // ============================================

    fun signInWithGoogle() {
        viewModelScope.launch {
            _authActionState.value = AuthActionState(
                action = AuthAction.SocialSignIn,
                isLoading = true
            )

            // TODO: Implement Google Sign In
            // This would typically involve Google Sign In SDK

            _authActionState.value = AuthActionState(
                action = AuthAction.SocialSignIn,
                isLoading = false,
                isSuccess = false,
                message = "Google Sign In not implemented yet"
            )
        }
    }

    fun signInWithApple() {
        viewModelScope.launch {
            _authActionState.value = AuthActionState(
                action = AuthAction.SocialSignIn,
                isLoading = true
            )

            // TODO: Implement Apple Sign In
            // This would typically involve Apple Sign In SDK

            _authActionState.value = AuthActionState(
                action = AuthAction.SocialSignIn,
                isLoading = false,
                isSuccess = false,
                message = "Apple Sign In not implemented yet"
            )
        }
    }


    // ============================================
    // VALIDATION FUNCTIONS
    // ============================================


    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    private fun validateFullName(fullName: String): String? {
        return when {
            fullName.isBlank() -> "Full name is required"
            fullName.length < 2 -> "Full name must be at least 2 characters"
            fullName.length > 50 -> "Full name must be less than 50 characters"
            else -> null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }

    // ============================================
    // UTILITY FUNCTIONS
    // ============================================

    private fun checkAuthStatus() {
        if (isUserLoggedIn) {
            getCurrentUser()
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    private fun clearFormStates() {
        _loginFormState.value = LoginFormState()
        _registerFormState.value = RegisterFormState()
        _authActionState.value = AuthActionState()
    }

    fun clearAuthState() {
        _authState.value = AuthState.Initial
    }

    fun clearAuthActionState() {
        _authActionState.value = AuthActionState()
    }

    // Clear user session completely
    fun clearSession() {
        viewModelScope.launch {
            clearUserSessionUseCase()
            _authState.value = AuthState.LoggedOut
            clearFormStates()
        }
    }
}
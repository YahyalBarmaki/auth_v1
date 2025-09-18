package com.example.auth_v1.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth_v1.domain.model.User
import com.example.auth_v1.domain.usecase.CheckAuthStatusUseCase
import com.example.auth_v1.domain.usecase.GetCurrentUserUseCase
import com.example.auth_v1.domain.usecase.LogoutUseCase
import com.example.auth_v1.domain.repositories.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    private val _mainState = MutableStateFlow<MainState>(MainState.Loading)
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    private val _selectedRoute = MutableStateFlow("home")
    val selectedRoute: StateFlow<String> = _selectedRoute.asStateFlow()

    val userFlow: StateFlow<User?> = getCurrentUserUseCase.getUserFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isUserLoggedIn: Boolean
        get() = checkAuthStatusUseCase()

    init {
        checkUserAuthentication()
    }

    fun onRouteSelected(route: String) {
        _selectedRoute.value = route
    }

    fun logout() {
        viewModelScope.launch {
            _mainState.value = MainState.Loading
            
            when (logoutUseCase()) {
                is Result.Success -> {
                    _mainState.value = MainState.LoggedOut
                }
                is Result.Error -> {
                    _mainState.value = MainState.LoggedOut
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase.getCurrentUser()) {
                is Result.Success -> {
                    _mainState.value = MainState.Success
                }
                is Result.Error -> {
                    // Si refresh échoue, probablement token expiré -> logout
                    logoutUseCase()
                    _mainState.value = MainState.LoggedOut
                }
                is Result.Loading -> {
                    _mainState.value = MainState.Loading
                }
            }
        }
    }

    private fun checkUserAuthentication() {
        if (isUserLoggedIn) {
            refreshUser()
        } else {
            _mainState.value = MainState.LoggedOut
        }
    }
}

sealed class MainState {
    object Loading : MainState()
    object Success : MainState()
    object LoggedOut : MainState()
    data class Error(val message: String) : MainState()
}
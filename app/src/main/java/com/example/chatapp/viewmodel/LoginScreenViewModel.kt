package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginScreenViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()


    // todo should use Fire Base
    fun validateUser(): Boolean {
        return uiState.value.loginDetails.userName == "1" && uiState.value.loginDetails.password == "1"
    }

    fun updateUiState(loginDetails: LoginDetails) {
        _uiState.update { it.copy(loginDetails = loginDetails) }
    }
}

data class LoginUIState(
    val loginDetails: LoginDetails = LoginDetails(),
)

// Details (Model). Holds string values that are displayed to the UI
data class LoginDetails(
    val userName: String = "",
    val password: String = "",
)
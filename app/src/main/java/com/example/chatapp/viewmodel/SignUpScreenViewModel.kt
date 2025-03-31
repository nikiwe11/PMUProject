package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    fun signIn() {
        viewModelScope.launch {
            authRepository.createUserWithEmailAndPassword(
                uiState.value.loginDetails.userName,
                uiState.value.loginDetails.password
            )
        }
    }

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()


    // todo should use Fire Base
    fun validateUser(): Boolean {
        Log.d("credentials","Username: ${uiState.value.loginDetails.userName}, password: ${uiState.value.loginDetails.password}")
        return uiState.value.loginDetails.userName.isNotEmpty() && uiState.value.loginDetails.password.isNotEmpty()
    }

    fun updateUiState(loginDetails: LoginDetails) {
        _uiState.update { it.copy(loginDetails = loginDetails) }
    }
}


data class SignUpUiState(
    val loginDetails: LoginDetails = LoginDetails(),
)

// Details (Model). Holds string values that are displayed to the UI
data class SignUpDetails(
    val userName: String = "",
    val password: String = "",
)

package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()


    fun attemptLogin(onSuccess: () -> Unit, onFail: () -> Unit) {

        val fb = FirebaseAuth.getInstance()
        fb.signInWithEmailAndPassword(
            uiState.value.loginDetails.email,
            uiState.value.loginDetails.password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                onSuccess()

            } else {
                onFail()
            }
        }

        val result = fb.currentUser != null
        Log.d("test24", "current user?: $result")


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
    val email: String = "",
    val password: String = "",
)
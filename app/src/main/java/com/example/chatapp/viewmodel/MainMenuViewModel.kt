package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
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
class MainMenuViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainMenuUiState())
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            _uiState.update { it.copy(userIsLogged = false) }
        } else {
            Log.d("test24", "fool! +${authRepository.currentUser}")
            var user: User? = null
            try {
                viewModelScope.launch {
                    user = userRepository.getUser(firebaseUser.uid)
                    if (user == null) {
                        Log.d("test24", "failed to get user")
                        // error
                    } else {
                        Log.d("test24", "user:${user}")
                        _uiState.update {
                            it.copy(userDetails = user!!.toUserDetails(), userIsLogged = true)
                        }
                        CurrentUser.name = user!!.name

                    }
                }

            } catch (e: Exception) {
                Log.d(
                    "test24",
                    "you found it u dumb payaso $e.user = ${authRepository.currentUser}"
                )
            }

        }
    }

    fun signOut() {
        authRepository.signOut()
        _uiState.update { it.copy(userIsLogged = false) }
    }
}

fun User.toUserDetails(): UserDetails {
    return UserDetails(
        name = name
    )
}

data class MainMenuUiState(
    val userIsLogged: Boolean = true,
    val userDetails: UserDetails = UserDetails(),
)

data class UserDetails(
    val name: String = "",
)

object CurrentUser {
    var name: String = ""
}
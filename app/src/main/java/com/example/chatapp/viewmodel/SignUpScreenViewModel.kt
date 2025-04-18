package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import  com.google.firebase.auth.UserProfileChangeRequest.Builder

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    fun signIn(onSuccessAction: () -> Unit, onErrorAction: (String) -> Unit) {

        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                uiState.value.signUpDetails.email,
                uiState.value.signUpDetails.password
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        it.updateProfile(
                            Builder()
                                .setDisplayName(uiState.value.signUpDetails.name)
                                .build()
                        ).addOnCompleteListener {
                            Log.d("test14", "yeyuh!")
                            var firebaseUser = FirebaseAuth.getInstance().currentUser
                            if (firebaseUser != null) {
                                // Create a new user object and save it to Firestore
                                Log.d("test14", firebaseUser.uid)
                                val user = User(
                                    id = firebaseUser.uid, // Use Firebase Auth UID
                                    name = uiState.value.signUpDetails.name
                                )
                                viewModelScope.launch {
                                    userRepository.createUser(user)
                                    Log.d("test24","ok..")
                                    onSuccessAction()
                                }


                            } else {
                                Log.d("test14", "null user ig")
                            }
                        }
                        return@addOnCompleteListener
                    }
                    Log.d("test14", "ono!")

                } else {
                    Log.d("test14", "failure: ")
                    Log.d("test14", "failure: ${task.exception?.message}")
                }
            }
//                authRepository.createUserWithEmailAndPassword(
//                    uiState.value.signUpDetails.email,
//                    uiState.value.signUpDetails.password
//                )


        } catch (e: FirebaseAuthException) {
            Log.d("test014", "oh no!: $e")
            onErrorAction(e.message ?: "Error")
        }
    }


    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()


    // todo should use Fire Base
    fun validateUser(): Boolean {
        Log.d(
            "credentials",
            "Username: ${uiState.value.signUpDetails.email}, password: ${uiState.value.signUpDetails.password}"
        )
        return uiState.value.signUpDetails.name.isNotEmpty() && uiState.value.signUpDetails.email.isNotEmpty() && uiState.value.signUpDetails.password.isNotEmpty()
    }

    fun updateUiState(signUpDetails: SignUpDetails) {
        _uiState.update { it.copy(signUpDetails = signUpDetails) }
    }
}


data class SignUpUiState(
    val signUpDetails: SignUpDetails = SignUpDetails(),
)

// Details (Model). Holds string values that are displayed to the UI
data class SignUpDetails(
    val name: String = "",
    val email: String = "",
    val password: String = "",
)

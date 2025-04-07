package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.UserRepository
import com.example.chatapp.screens.ChatDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val chatId: String? = savedStateHandle[ChatDestination.chatIdArg]

    init {
        Log.d("test74", "chat id...:$chatId")
        if(chatId == null){
            Log.d("test74","null id!")
        }
        else{
            viewModelScope.launch {
               val user = userRepository.getUser(chatId)
                if(user !=null){
                    _uiState.update { it.copy(user = user) }
                }
                else {
                    Log.d("test74","cant get user!")
                }

            }
        }

    }
}

data class ChatUiState(
    val user: User = User(),
)
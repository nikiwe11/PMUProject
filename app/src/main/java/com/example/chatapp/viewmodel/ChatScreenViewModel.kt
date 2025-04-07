package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Message
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
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val userId: String? = savedStateHandle[ChatDestination.chatIdArg]
    var chatId = ""

    init {

        Log.d("test74", "chat id...:$userId")
        if (userId == null) {
            Log.d("test74", "null id!")
        } else {
            viewModelScope.launch {
                val user = userRepository.getUser(userId)

                if (user != null) {
                    val chatIds = listOf(user.id, CurrentUser.id).sortedDescending()
                    chatId = chatIds[0] + "_" + chatIds[1]

                    _uiState.update { it.copy(friendUser = user, chatId = chatId) }
                    observeMessages()
                } else {
                    Log.d("test74", "cant get user!")
                }

            }
        }

    }

    private fun observeMessages() {
        viewModelScope.launch {
            userRepository.listenToMessages(uiState.value.chatId)
                .collect { messages ->
                    _uiState.update { current ->
                        current.copy(messages = messages)
                    }
                }
        }
    }

    fun updateMessages() {
        viewModelScope.launch {
            val messages = userRepository.getMessages(uiState.value.chatId)
            _uiState.update { it.copy(messages = messages) }
        }


    }

    fun updateUiState(messageDetails: MessageDetails) {
        _uiState.update { it.copy(messageDetails = messageDetails) }
    }

    fun getSenderName(message: Message) : String {
        return if (message.senderId == uiState.value.friendUser.id){
            uiState.value.friendUser.name
        } else {
            CurrentUser.name
        }
    }



    fun sendMessage() {
        val timeStamp = LocalDate.now().toString() + LocalTime.now().toString()
        viewModelScope.launch {
            userRepository.sendMessage(
                uiState.value.chatId,
                Message(
                    id = CurrentUser.id + System.currentTimeMillis().toString(),
                    senderId = CurrentUser.id,
                    text = uiState.value.messageDetails.message,
                    timeStamp = timeStamp
                )
            )
            updateMessages()

        }
        // Clear input field
        _uiState.update { it.copy(messageDetails = MessageDetails()) }

    }
}

data class ChatUiState(
    val chatId: String = "",
    val messageDetails: MessageDetails = MessageDetails(),
    val friendUser: User = User(),
    val messages: List<Message> = listOf(),
)

data class MessageDetails(
    val message: String = "",
)
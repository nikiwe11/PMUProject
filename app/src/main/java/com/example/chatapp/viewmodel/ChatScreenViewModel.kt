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
    private var lastMessageTimestamp: String? = null
    private val messagesPerPage = 20

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
                    loadInitialMessages()
                    observeNewMessages()
                } else {
                    Log.d("test74", "cant get user!")
                }

            }
        }

    }

    fun observeNewMessages() {
        viewModelScope.launch {
            userRepository.listenToMessages(chatId).collect { newMessages ->
                val existingIds = uiState.value.messages.map { it.id }.toSet()
                val filtered = newMessages.filterNot { existingIds.contains(it.id) }
                if (filtered.isNotEmpty()) {

                    _uiState.update {
                        val updatedMessages = (it.messages + filtered).sortedBy { msg -> msg.timeStamp }
                        it.copy(messages = updatedMessages)
                    }
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
    fun loadInitialMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            val messages = userRepository.getMessagesBatch(chatId, messagesPerPage.toLong(), null)
            if (messages.isNotEmpty()) {
                lastMessageTimestamp = messages.last().timeStamp
                _uiState.update {
                    it.copy(
                        messages = messages.reversed(), // oldest at top
                        shouldScrollToBottom = true      // 👈 Trigger scroll on first load
                    )
                }
            }
            _uiState.update { it.copy(isLoadingMore = false) }
        }
    }
    fun loadMoreMessages() {
        if (uiState.value.isLoadingMore || lastMessageTimestamp == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            val olderMessages = userRepository.getMessagesBatch(chatId, messagesPerPage.toLong(), lastMessageTimestamp)
            if (olderMessages.isNotEmpty()) {
                lastMessageTimestamp = olderMessages.last().timeStamp
                _uiState.update {
                    it.copy(messages = olderMessages.reversed() + it.messages)
                }
            }
            _uiState.update { it.copy(isLoadingMore = false) }
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
        val timeStamp = java.time.Instant.now().toString()
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
        _uiState.update { it.copy(messageDetails = MessageDetails(), shouldScrollToBottom = true) }

    }
    fun resetScrollFlag() {
        _uiState.update { it.copy(shouldScrollToBottom = false) }
    }
}

data class ChatUiState(
    val isLoadingMore: Boolean = false,
    val shouldScrollToBottom: Boolean = true,
    val chatId: String = "",
    val messageDetails: MessageDetails = MessageDetails(),
    val friendUser: User = User(),
    val messages: List<Message> = listOf(),
)

data class MessageDetails(
    val message: String = "",
)
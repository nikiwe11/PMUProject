package com.example.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        val firebaseUser =  FirebaseAuth.getInstance().currentUser
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
                        _uiState.update { it.copy(userIsLogged = false) }
                        // error
                    } else {
                        val friendsList: List<User> = userRepository.getFriendsList(user!!.id)
                        Log.d("test74","friends: $friendsList")

                        Log.d("test24", "user:${user}")
                        CurrentUser.id = user!!.id
                        CurrentUser.name = user!!.name
                        CurrentUser.friends = friendsList
                        _uiState.update {
                            it.copy(userDetails = user!!.toUserDetails(), userIsLogged = true, loading = false)
                        }
                        loadChats()
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
    fun loadChats(){
        viewModelScope.launch {
            CurrentUser.friends.forEach { friend ->
                Log.d("test184","friend: ${friend}")
                val chat = userRepository.getChat(CurrentUser.id,friend.id)
                if(chat!=null){
                    val newChats = uiState.value.chats + chat
                    _uiState.update { it.copy(chats = newChats) }
                }

            }

        }
    }

    fun getChatByFriendId(friendId: String): Chat? {
        val sortedIds = listOf(CurrentUser.id, friendId).sortedDescending()
        val chatId = "${sortedIds[0]}_${sortedIds[1]}"
        uiState.value.chats.forEach { chat ->
            Log.d("test184","matching $chatId with ${chat.id}")
            if(chatId==chat.id){
                Log.d("test184","returning $chat")
                return chat
            }
        }
        return null
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
    val loading: Boolean = true,
    val userDetails: UserDetails = UserDetails(),
    val chats: List<Chat> = listOf()
)

data class UserDetails(
    val name: String = "",
)

object CurrentUser {
    var id: String = ""
    var name: String = ""
    var friends: List<User> = listOf()
    fun getFriendById(_id: String) : User ?{
        friends.forEach { friend ->
            if (friend.id==_id){
                return friend
            }
        }
        return null
    }
}
package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddFriendUiState())
    val uiState: StateFlow<AddFriendUiState> = _uiState.asStateFlow()


    fun onSearchTextChange(text: String) {
        _uiState.update { it -> it.copy(searchText = text) }
    }

    fun updateList() {
        viewModelScope.launch {
            val result = if (_uiState.value.searchText.isEmpty()) {
                listOf<User>()
            } else { // todo may be too fast-search with each type?
                userRepository.search(uiState.value.searchText)
            }
            _uiState.update { it.copy(usersDisplayed = result) }
        }
    }

    fun addFriend(user: User) {
        viewModelScope.launch {
            // add the friend by this user
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            userRepository.addFriend(
                currentUserId = firebaseUser!!.uid,
                User(id = user.id, name = user.name)
            )
            // this user is added by the friend afterwards
            userRepository.addFriend(
                currentUserId = user.id,
                User(id = firebaseUser.uid,name = CurrentUser.name)
            )
        }
    }
}

data class AddFriendUiState(
    val searchText: String = "",

    val usersDisplayed: List<User> = listOf(),
) {
    fun getUsersDisplayed(updateList: () -> Unit): List<User> {
        updateList()
        return this.usersDisplayed
    }
}

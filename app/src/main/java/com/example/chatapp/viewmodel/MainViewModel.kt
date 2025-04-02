package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState()) // State is edited trough this
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow() // Observable state

    fun setErrorState(showError: Boolean,error: String) {
        _uiState.update { it.copy(showError = showError,error = error) }
    }

}

data class MainUiState(
    val showError: Boolean = false,
    val error: String = ""
)
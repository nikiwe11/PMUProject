package com.example.chatappsingle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel() :
    ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()




    fun updateUiState(settingsDetails: SettingsDetails) {
        _uiState.update {
            it.copy(settingsDetails = settingsDetails)
        }
    }
}

data class SettingsDetails(
    val ip: String = "",
    val port: String = "",
    val language: String = "",
    val debugReceipt: Boolean = false,
    val useDarkTheme: Boolean = false,
)

data class SettingsUiState(
    val settingsDetails: SettingsDetails = SettingsDetails(),
)


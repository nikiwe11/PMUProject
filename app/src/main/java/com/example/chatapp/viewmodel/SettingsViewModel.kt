package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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


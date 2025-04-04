package com.example.chatapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.viewmodel.MainMenuViewModel
import com.example.chatapp.viewmodel.MainViewModel

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToAddFriend: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    if (!uiState.userIsLogged) {
        navigateToLogin()
    }

    Row {
        Text("Hello ${uiState.userDetails.name}")
        Button(onClick = {viewModel.signOut()}){Text("sign out")}
        Button(onClick = {navigateToAddFriend()}) { Text("Add Friend")}
    }
}
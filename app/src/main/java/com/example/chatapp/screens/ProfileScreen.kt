package com.example.chatapp.screens

import CustomBottomBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.viewmodel.MainMenuViewModel

@Composable
fun ProfileScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    navigateToMainMenu: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            CustomBottomBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { navigateToMainMenu() }) {
                        Text(text="Chats",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Profile",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text("Your profile")
            Button(onClick = { navigateToMainMenu() }) {
                Text("Go back")
            }
            // TODO: trqa se naprai da bachka sign out buttona
            Button(
                onClick = { viewModel.signOut() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}
package com.example.chatapp.screens

import CustomBottomBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.general.Constants
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.general.selectFromTheme
import com.example.chatapp.viewmodel.MainMenuViewModel

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToAddFriend: () -> Unit,
    navigateToChat:() -> Unit,
    navigateToProfile:()-> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    if (!uiState.userIsLogged) {
        navigateToLogin()
    }
    Column(

        verticalArrangement = Arrangement.Bottom
    ) {

//        Row() {
//
//            // TODO: trqa se premesti sign out
//            Button(onClick = { viewModel.signOut() }) { Text("sign out") }
//            Button(onClick = { navigateToAddFriend() }) { Text("AddFriend") }
        Scaffold(
            topBar = {
                TransparentSurfaceWithGradient(
                    modifier = Modifier.fillMaxWidth(),
                    alpha = 0.42f,
                    brush = selectFromTheme(
                        Brush.horizontalGradient(colors = Constants.Gradient.GREEN_TO_BROWN.reversed()),
                        Brush.horizontalGradient(colors = Constants.Gradient.RED_TO_BLACK.reversed())
                    ),
                    border = null,
                    roundedCornerShape = RoundedCornerShape(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left side (empty for now or add back button if needed)
                        Spacer(modifier = Modifier.width(48.dp))

                        // Center title


                        // Right side - Add button
                        TextButton(
                            onClick = navigateToAddFriend,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Add",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            bottomBar = {
                CustomBottomBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            // TODO: да се добави бейсик ахх чат иконка
                            text = "Chats",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,

                            )
                        Spacer(modifier = Modifier.width(8.dp))
                        // TODO: да се добави иконка за профил, било то дефалтна или избрана от човека
                        TextButton(onClick = { navigateToProfile() }) {
                            Text(
                                text = "Profile",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )

                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // TODO: кат се направят чатовете да се използва за да те изпрати към тях
                Button(
                    onClick = { navigateToChat() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                ) {
                    Text("Chat")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // TODO: trqa se mahne sign out butona i da ide na ProfileScreen
                        Button(onClick = { viewModel.signOut() }) {
                            Text("sign out")
                        }
                    }
                }
            }
        }
    }
}
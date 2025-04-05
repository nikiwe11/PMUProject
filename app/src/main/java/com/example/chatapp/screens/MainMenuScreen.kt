package com.example.chatapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.general.TransparentSurfaceWithGradient
import com.example.chatapp.viewmodel.MainMenuViewModel
import com.example.chatapp.viewmodel.MainViewModel

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

        verticalArrangement = Arrangement.Bottom){

        Row(){

            // TODO: trqa se premesti sign out
            Button(onClick = { viewModel.signOut() }) { Text("sign out") }
            Button(onClick = { navigateToAddFriend() }) { Text("AddFriend") }


        }
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),

            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally){
            Row(
                modifier= Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){Button(onClick = {navigateToChat()}){Text("Chats")}
                Button(onClick = {navigateToProfile()}){Text("Profile")}}}

    }



}

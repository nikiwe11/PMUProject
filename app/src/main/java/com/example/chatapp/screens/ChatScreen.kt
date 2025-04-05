package com.example.chatapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    navigateToMainMenu: () -> Unit,

    ) {
    Scaffold (){ padding ->
        Column (modifier = Modifier.padding(padding)){      Text("Hello nougat")
            Row {


                Button(onClick = {navigateToMainMenu()}) { Text("b(l)ack button")}
            } }


    }

}
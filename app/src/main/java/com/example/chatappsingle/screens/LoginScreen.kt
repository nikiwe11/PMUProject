package com.example.chatappsingle.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onClick: () -> Unit
){
    Text("Hello login screen!")
    Button(onClick = onClick){
        Text("Navigate somewhere else")
    }
}
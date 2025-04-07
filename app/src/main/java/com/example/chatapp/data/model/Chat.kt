package com.example.chatapp.data.model

data class Chat(
    val id: String = "",
    val participantIds: List<String> = listOf(),
    val lastMessage: Message = Message(),
)
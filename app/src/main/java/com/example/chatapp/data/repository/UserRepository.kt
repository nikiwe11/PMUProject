package com.example.chatapp.data.repository

import com.example.chatapp.data.datasource.UserRemoteDataSource
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend fun getUser(userId: String): User? {
        return userRemoteDataSource.getUserData(userId)
    }

    suspend fun addFriend(currentUserId: String, friend: User) {
        return userRemoteDataSource.addFriend(currentUserId, friend)
    }

    suspend fun getFriendsList(userId: String): List<User> {
        return userRemoteDataSource.getFriendsList(userId)
    }

    suspend fun createChat(user1Id: String, user2Id: String): String {
        return userRemoteDataSource.createChat(user1Id, user2Id)
    }
    suspend fun getChat(user1Id: String, user2Id: String) : Chat? {
        return userRemoteDataSource.getChat(user1Id = user1Id,user2Id=user2Id)
    }

    suspend fun sendMessage(chatId: String, message: Message): String {
        return userRemoteDataSource.sendMessage(chatId, message)
    }
    suspend fun getMessagesBatch(chatId: String, limit: Long, startAfterTimestamp: String?): List<Message> {
        return userRemoteDataSource.getMessagesBatch(chatId, limit, startAfterTimestamp)
    }
    suspend fun getMessages(chatId: String): List<Message> {
        return userRemoteDataSource.getMessages(chatId)
    }
    fun listenToMessages(chatId: String) : Flow<List<Message>>{
        return userRemoteDataSource.listenToMessages(chatId)
    }

    suspend fun search(name: String): List<User> {
        return userRemoteDataSource.searchUserByUsername(name)
    }

    suspend fun createUser(user: User): String {
        return userRemoteDataSource.create(user)
    }

}
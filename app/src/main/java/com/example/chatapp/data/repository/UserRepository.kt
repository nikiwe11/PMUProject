package com.example.chatapp.data.repository

import com.example.chatapp.data.datasource.UserRemoteDataSource
import com.example.chatapp.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend fun getUser(userId: String): User? {
        return userRemoteDataSource.getUserData(userId)
    }
    suspend fun addFriend(currentUserId: String, friend: User) {
        return userRemoteDataSource.addFriend(currentUserId,friend)
    }

    suspend fun search(name: String): List<User> {
        return userRemoteDataSource.searchUserByUsername(name)
    }

    suspend fun createUser(user: User): String {
        return userRemoteDataSource.create(user)
    }

}
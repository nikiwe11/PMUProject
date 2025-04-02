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

    suspend fun createUser(user: User): String {
        return userRemoteDataSource.create(user)
    }

}
package com.example.chatapp.data.datasource

import android.util.Log
import com.example.chatapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTodoItems(currentUserIdFlow: Flow<String?>): Flow<List<User>> {
        return currentUserIdFlow.flatMapLatest { ownerId ->
            firestore
                .collection(TODO_ITEMS_COLLECTION)
                .whereEqualTo(OWNER_ID_FIELD, ownerId)
                .dataObjects()
        }
    }
    suspend fun searchUserByUsername(name: String): List<User> {
        return try {
            val snapshot = firestore.collection(USER_OWNER)
                .whereEqualTo("name", name)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val id = doc.id // or doc.getString("id") if you're storing it in fields
                User(id = id, name = name)
            }
        } catch (e: Exception) {
            Log.e("FirestoreSearch", "Error searching users: ${e.message}")
            emptyList()
        }
    }
    suspend fun addFriend(currentUserId: String, friend: User) {
        try {
            firestore.collection(USER_OWNER)
                .document(currentUserId)
                .collection(FRIENDS)
                .document(friend.id)
                .set(friend)
                .await()
            Log.d("FirestoreFriend", "Added ${friend.name} as friend.")
        } catch (e: Exception) {
            Log.e("FirestoreFriend", "Failed to add friend: ${e.message}")
        }
    }
    suspend fun getFriendsList(userId: String): List<User> {
        return try {
            firestore.collection(USER_OWNER)
                .document(userId)
                .collection(FRIENDS)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val id = doc.id
                    User(id = id, name = name)
                }
        } catch (e: Exception) {
            Log.e("FirestoreFriend", "Failed to load friends: ${e.message}")
            emptyList()
        }
    }
    suspend fun getUserData(userId: String): User? {
        Log.d("test24","whhats going on bruv...${userId}")
        val snapshot = firestore.collection(USER_OWNER).document(userId).get().await()
        if (snapshot.exists()) {
            val userMap = snapshot.data
            Log.d("test24", "User data: $userMap")
            val name = userMap?.get("name") as? String?: "empty"
            val id =userMap?.get("id") as? String?: "123"
            Log.d("test24","name:$name,id:$id")
            return User(id = id,name = name)
        }
        return User(id = "1",name = "guz")
    }
    suspend fun create(user: User): String {
        try {
            firestore.collection(USER_OWNER)
                .document(user.id)  // Use UID as document ID
                .set(user)  // Store user data
                .await()
            Log.d("Firestore", "User successfully saved with UID: ${user.id}")
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error saving user: ", e)
        }
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        Log.d("test14","UMIRAM: ${firebaseUser.toString()}")
        Log.d("test14","just to confirm: ${user}")
//        try{
//             firestore.collection(USER_OWNER).document(user.id).set(user).await()
//
//        } catch(e: FirebaseFirestoreException){
//            Log.d("test14","bruh: $e")
//        }
        return ""
    }

//    suspend fun getTodoItem(itemId: String): com.google.firebase.example.makeitso.data.model.TodoItem? {
//        return firestore.collection(TODO_ITEMS_COLLECTION).document(itemId).get().await().toObject()
//    }
//
//    suspend fun create(todoItem: com.google.firebase.example.makeitso.data.model.TodoItem): String {
//        return firestore.collection(TODO_ITEMS_COLLECTION).add(todoItem).await().id
//    }
//
//    suspend fun update(todoItem: com.google.firebase.example.makeitso.data.model.TodoItem) {
//        firestore.collection(TODO_ITEMS_COLLECTION).document(com.google.firebase.example.makeitso.data.model.TodoItem.id).set(todoItem).await()
//    }
//
//    suspend fun delete(itemId: String) {
//        firestore.collection(TODO_ITEMS_COLLECTION).document(itemId).delete().await()
//    }

    companion object {
        private const val USER_OWNER = "userOwner"
        private const val OWNER_ID_FIELD = "ownerId"
        private const val FRIENDS = "friends"
        private const val TODO_ITEMS_COLLECTION = "todoitems"
    }
}
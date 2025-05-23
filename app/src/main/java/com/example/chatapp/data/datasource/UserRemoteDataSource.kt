package com.example.chatapp.data.datasource

import android.util.Log
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun getTodoItems(currentUserIdFlow: Flow<String?>): Flow<List<User>> {
//        return currentUserIdFlow.flatMapLatest { ownerId ->
//            firestore
//                .collection(TODO_ITEMS_COLLECTION)
//                .whereEqualTo(OWNER_ID_FIELD, ownerId)
//                .dataObjects()
//        }
//    }
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
    suspend fun createChat(user1Id: String, user2Id: String): String {
        val sortedIds = listOf(user1Id, user2Id).sortedDescending()
        val chat = Chat(
            id = sortedIds[0]+"_"+sortedIds[1],
            participantIds = sortedIds, // Sorted to avoid duplicates
            lastMessage = Message(),
        )

        // Check if chat already exists
        val existing = firestore.collection(CHATS)
            .whereEqualTo("participantIds", chat.participantIds)
            .get()
            .await()

        if (!existing.isEmpty) {
            Log.d("test184","existing: ${existing.documents}")
            return existing.documents[0].id // Return existing chat ID
        }

        // Create new chat
        val docRef = firestore.collection(CHATS)
            .document(chat.id) // Use your custom ID
            .set(chat)         // Save the chat data
            .await()

        return chat.id
    }
    suspend fun getChat(user1Id: String, user2Id: String): Chat? {
        // Sort the participant IDs to avoid issues with different orders of user IDs
        val sortedIds = listOf(user1Id, user2Id).sortedDescending()

        // Construct a custom chat ID based on sorted participant IDs
        val chatId = "${sortedIds[0]}_${sortedIds[1]}"

        // Query Firestore to check if a chat already exists with these participants
        Log.d("test184", "looking for id: $chatId")
        val existingChatSnapshot = firestore.collection(CHATS)
            .whereEqualTo("id", chatId)  // Search for chat with these sorted participant IDs
            .get()
            .await()

        // If a chat is found, map it to a Chat object
        if (!existingChatSnapshot.isEmpty) {
            Log.d("test184", "found snapshot")
            val chatDocument = existingChatSnapshot.documents[0]

            // Manually map the document to a Chat object
            val id = chatDocument.id // Chat ID (Firestore document ID)
            val participantIds = chatDocument.get("participantIds") as? List<String> ?: listOf()
            val lastMessageData = chatDocument.get("lastMessage") as? Map<*, *>? // Assuming lastMessage is stored as a map
            Log.d("test184","last msg: $lastMessageData")
            // Manually map the lastMessage
            val lastMessage = if (lastMessageData != null) {
                val text = lastMessageData["text"] as? String ?: ""
                val senderId = lastMessageData["senderId"] as? String ?: ""
                val lastChatId = lastMessageData["id"] as? String ?: ""
                val timeStamp = lastMessageData["timeStamp"] as? String ?: ""
                Message(
                    id = lastChatId, // You can generate or leave it empty
                    senderId = senderId,
                    text = text,
                    timeStamp = timeStamp // Populate timestamp if available in the lastMessageData
                )
            } else {
                Message() // Default message if no lastMessage is found
            }

            // Return the Chat object
            return Chat(
                id = id,
                participantIds = participantIds,
                lastMessage = lastMessage
            )
        }

        // If no chat exists, return null
        return null
    }

    suspend fun sendMessage(chatId: String, message: Message): String {
        Log.d("test74","chat id: $chatId and message : $message")
        val messageRef = firestore
            .collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .add(message)
            .await()

        // Optionally update chat metadata
        firestore.collection(CHATS).document(chatId).update(
            mapOf(
                "lastMessage" to message
            )
        )

        return messageRef.id
    }
    fun listenToMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .orderBy("timeStamp") // assuming you have a timestamp field
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(messages)
            }

        awaitClose {
            listener.remove()
        }
    }
    suspend fun getMessagesBatch(
        chatId: String,
        limit: Long = 20,
        startAfterTimestamp: String? = null
    ): List<Message> {
        val query = firestore.collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)

        val finalQuery = if (startAfterTimestamp != null) {
            query.startAfter(startAfterTimestamp)
        } else {
            query
        }

        val snapshot = finalQuery.get().await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Message::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun getMessages(chatId: String): List<Message> {
        val snapshot = firestore.collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .orderBy("timeStamp")
            .get()
            .await()

        val messages = snapshot.documents.mapNotNull { doc ->
            val data = doc.data
            Message(
                id = doc.id,
                senderId = data?.get("senderId") as? String ?: "",
                text = data?.get("text") as? String ?: "",
                timeStamp = data?.get("timeStamp") as? String ?: ""
            )
        }
        Log.d("test74","all messages: $messages")
        return messages
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
        return User(id = "1",name = "failed to get user")
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
        private const val CHATS = "chats"
        private const val MESSAGES = "messages"
    }
}


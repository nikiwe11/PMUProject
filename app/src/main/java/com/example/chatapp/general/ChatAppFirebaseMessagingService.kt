package com.example.chatapp.general
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.chatapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ChatAppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Called when a message is received (foreground/background)
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            showNotification(it.title, it.body)
        }
    }

    override fun onNewToken(token: String) {
        // This method is called when a new token is generated
        Log.d("FCM", "New token generated: $token")

        // You should send this token to your backend to associate it with the user's account
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // You can send this token to your backend API to store it
        // for push notifications targeted to specific users
        // Example:
        // apiService.updateUserPushToken(userId, token)
    }
    private fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel (for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "chat_channel",
                "Chat Messages",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "chat_channel")
            .setContentTitle(title ?: "New Message")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Your icon
            .build()

        notificationManager.notify(1, notification)
    }
}

package com.nihatmahammadli.abbmobile.presentation.components.notification

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->
            val title = notification.title ?: "No Title"
            val body = notification.body ?: "No Message"
            NotificationHelper.generateNotification(applicationContext, title, body)
        }
    }
}

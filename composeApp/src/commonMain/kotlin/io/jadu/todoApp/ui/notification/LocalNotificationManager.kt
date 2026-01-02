package io.jadu.todoApp.ui.notification

expect class LocalNotificationManager {
    fun hasPermission(): Boolean
    suspend fun requestPermission(activity: Any?): Boolean
    fun showNotification(title: String, body: String)
}
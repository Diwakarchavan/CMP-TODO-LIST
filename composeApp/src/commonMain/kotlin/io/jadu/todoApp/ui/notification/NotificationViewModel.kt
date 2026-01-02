package io.jadu.todoApp.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotificationViewModel(private val manager: LocalNotificationManager): ViewModel() {

    fun onEnableNotificationsClicked(activity: Any? = null) {
        viewModelScope.launch {
            if (!manager.hasPermission()) {
                val granted = manager.requestPermission(activity)

                if (granted) {
                    manager.showNotification("Success", "Notifications enabled!")
                }
            } else {
                manager.showNotification("Success", "Notifications enabled!")
            }
        }
    }
}
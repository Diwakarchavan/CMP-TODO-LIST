package io.jadu.todoApp.ui.notification

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Android implementation to get the current Activity from Composable context.
 */
@Composable
actual fun rememberActivity(): Any? {
    val context = LocalContext.current
    return context as? Activity
}


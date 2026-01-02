package io.jadu.todoApp.ui.notification

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic way to get the current Activity (or equivalent) from Composable context.
 * Returns the Activity on Android, null on iOS (not needed there).
 */
@Composable
expect fun rememberActivity(): Any?


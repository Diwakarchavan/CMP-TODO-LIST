package io.jadu


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.jadu.todoApp.data.local.ThemeRepository
import io.jadu.todoApp.ui.screens.MainScreen
import io.jadu.todoApp.ui.theme.TodoAppTheme
import org.koin.compose.koinInject

@Composable
fun App() {
    val themeRepository: ThemeRepository = koinInject()
    val isDarkMode by themeRepository.isDarkMode.collectAsState(initial = null)
    val systemDarkTheme = isSystemInDarkTheme()

    val resolvedDarkMode = isDarkMode ?: systemDarkTheme

    TodoAppTheme(darkTheme = resolvedDarkMode) {
        MainScreen()
    }
}
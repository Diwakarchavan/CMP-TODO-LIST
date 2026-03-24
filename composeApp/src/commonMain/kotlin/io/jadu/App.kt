package io.jadu


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.jadu.todoApp.data.local.ThemeRepository
import io.jadu.todoApp.ui.screens.MainScreen
import io.jadu.todoApp.ui.theme.TodoAppTheme
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val themeRepository: ThemeRepository = koinInject()
    val isDarkMode by themeRepository.isDarkMode.collectAsState(initial = false)

    TodoAppTheme(darkTheme = isDarkMode) {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}
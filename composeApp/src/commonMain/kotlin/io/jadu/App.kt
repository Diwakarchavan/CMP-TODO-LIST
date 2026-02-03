package io.jadu


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.jadu.todoApp.ui.screens.MainScreen
import io.jadu.todoApp.ui.theme.TodoAppTheme

@Composable
@Preview
fun App() {
    TodoAppTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}
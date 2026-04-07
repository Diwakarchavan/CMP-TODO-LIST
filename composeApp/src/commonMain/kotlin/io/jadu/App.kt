package io.jadu


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.jadu.todoApp.ui.screens.MainScreen
import io.jadu.todoApp.ui.theme.TodoAppTheme

@Composable
@Preview
fun App() {
    TodoAppTheme {
        MainScreen()
    }
}
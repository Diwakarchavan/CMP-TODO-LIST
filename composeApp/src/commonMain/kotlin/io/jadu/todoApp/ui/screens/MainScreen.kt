package io.jadu.todoApp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.jadu.todoApp.ui.route.NavRoute
import io.jadu.todoApp.ui.route.RootNavGraph
import io.jadu.todoApp.ui.screens.navigations.RootNavKeySaver
import io.jadu.todoApp.ui.screens.navigations.RootNavigation
import io.jadu.todoApp.ui.theme.TodoColors
import io.jadu.todoApp.ui.viewModel.OnBoardingViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainScreen() {
    val onBoardingViewModel: OnBoardingViewModel = koinInject()
    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var startDestination by rememberSaveable(stateSaver = RootNavKeySaver) {
        mutableStateOf(RootNavGraph.Onboarding)
    }

    LaunchedEffect(Unit) {
        scope.launch {
            val currentRoute = onBoardingViewModel.getCurrentRoute()
            startDestination = when(currentRoute) {
                NavRoute.Onboarding -> {
                    RootNavGraph.Onboarding
                }

                else -> {
                    RootNavGraph.BottomNavBar
                }

            }
            isLoading = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TodoColors.Primary.color)
            }
        } else {
            RootNavigation(
                startDestination = startDestination
            )
        }
    }

}
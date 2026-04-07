package io.jadu.todoApp.ui.screens.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.jadu.todoApp.ui.route.RootNavGraph

/**
 * Root Navigation that decides between Onboarding and BottomNavBar app flow
 */
@Composable
fun RootNavigation(
    startDestination: RootNavGraph
) {
    var currentGraph by rememberSaveable(startDestination, stateSaver = RootNavKeySaver) {
        mutableStateOf(startDestination)
    }

    when (currentGraph) {
        is RootNavGraph.Onboarding -> {
            OnboardingNavigation(
                onOnboardingComplete = {
                    currentGraph = RootNavGraph.BottomNavBar
                }
            )
        }

        is RootNavGraph.BottomNavBar -> {
            BottomBarNavigation()
        }
    }
}

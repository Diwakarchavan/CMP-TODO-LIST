package io.jadu.todoApp.ui.screens.navigations

import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import io.jadu.todoApp.ui.route.NavRoute
import io.jadu.todoApp.ui.route.RootNavGraph

/** Saves/restores the active top-level tab route across process death. */
internal val TopLevelNavKeySaver: Saver<NavKey, String> = Saver(
    save = { navKey ->
        navKey::class.simpleName ?: NavRoute.Home::class.simpleName!!
    },
    restore = { name ->
        when (name) {
            NavRoute.Home::class.simpleName -> NavRoute.Home
            NavRoute.TaskScreen::class.simpleName -> NavRoute.TaskScreen
            NavRoute.AddProject::class.simpleName -> NavRoute.AddProject
            NavRoute.CategorySphereScreen::class.simpleName -> NavRoute.CategorySphereScreen
            NavRoute.SettingsScreen::class.simpleName -> NavRoute.SettingsScreen
            NavRoute.Onboarding::class.simpleName -> NavRoute.Onboarding
            else -> NavRoute.Home
        }
    }
)

/** Saves/restores the tab-switch history list across process death. */
internal val TabHistorySaver: Saver<List<NavKey>, String> = Saver(
    save = { list ->
        list.joinToString(",") { it::class.simpleName ?: "" }
    },
    restore = { encoded ->
        if (encoded.isBlank()) emptyList()
        else encoded.split(",").mapNotNull { name ->
            when (name) {
                NavRoute.Home::class.simpleName -> NavRoute.Home
                NavRoute.TaskScreen::class.simpleName -> NavRoute.TaskScreen
                NavRoute.AddProject::class.simpleName -> NavRoute.AddProject
                NavRoute.CategorySphereScreen::class.simpleName -> NavRoute.CategorySphereScreen
                NavRoute.SettingsScreen::class.simpleName -> NavRoute.SettingsScreen
                else -> null
            }
        }
    }
)

/** Saves/restores the app's current graph (Onboarding vs BottomBar) across process death. */
internal val RootNavKeySaver: Saver<RootNavGraph, String> = Saver(
    save = { graph ->
        graph::class.simpleName ?: RootNavGraph.Onboarding::class.simpleName!!
    },
    restore = { name ->
        when (name) {
            RootNavGraph.Onboarding::class.simpleName -> RootNavGraph.Onboarding
            RootNavGraph.BottomNavBar::class.simpleName -> RootNavGraph.BottomNavBar
            else -> RootNavGraph.Onboarding
        }
    }
)

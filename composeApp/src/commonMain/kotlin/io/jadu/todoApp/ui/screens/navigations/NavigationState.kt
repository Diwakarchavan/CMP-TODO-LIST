package io.jadu.todoApp.ui.screens.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration

import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import io.jadu.todoApp.ui.route.NavRoute
import io.jadu.todoApp.ui.route.RootNavGraph
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val navSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(NavRoute.Onboarding::class, NavRoute.Onboarding.serializer())
        subclass(NavRoute.Home::class, NavRoute.Home.serializer())
        subclass(NavRoute.TaskScreen::class, NavRoute.TaskScreen.serializer())
        subclass(NavRoute.EditTodo::class, NavRoute.EditTodo.serializer())
        subclass(NavRoute.AddProject::class, NavRoute.AddProject.serializer())
        subclass(NavRoute.CategorySphereScreen::class, NavRoute.CategorySphereScreen.serializer())
        subclass(NavRoute.SettingsScreen::class, NavRoute.SettingsScreen.serializer())
        subclass(NavRoute.AboutUsScreen::class, NavRoute.AboutUsScreen.serializer())
        subclass(NavRoute.TestScreen::class, NavRoute.TestScreen.serializer())

        subclass(RootNavGraph.Onboarding::class, RootNavGraph.Onboarding.serializer())
        subclass(RootNavGraph.BottomNavBar::class, RootNavGraph.BottomNavBar.serializer())
    }
}

/**
 * Remembers a [NavigationState] across configuration changes and process death.
 *
 * @param startRoute Initial destination.
 * @param topLevelRoutes Destinations that each maintain their own independent backstack.
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: List<NavKey>
): NavigationState {

    val topLevelRoute = rememberSaveable(startRoute, stateSaver = TopLevelNavKeySaver) {
        mutableStateOf(startRoute)
    }

    val tabHistory = rememberSaveable(startRoute, stateSaver = TabHistorySaver) {
        mutableStateOf(listOf())
    }

    val backStacks = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(
            SavedStateConfiguration { serializersModule = navSerializersModule },
            key
        )
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            tabHistoryState = tabHistory,
            backStacks = backStacks
        )
    }
}

/**
 * Holds multiple independent backstacks for top-level (tab) navigation.
 *
 * @param startRoute Root destination — popping here exits the app.
 * @param topLevelRoute Saveable state for the currently active tab.
 * @param tabHistoryState Saveable state for tab-switch history.
 * @param backStacks Per-tab backstacks.
 */
class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    tabHistoryState: MutableState<List<NavKey>>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute by topLevelRoute
    var tabHistory by tabHistoryState

    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }

    /**
     * Resolves the current active route from the top-most entry of the current tab's stack.
     */
    val currentRoute: NavKey
        get() = backStacks[topLevelRoute]?.last() ?: topLevelRoute
}

/**
 * Converts the active [NavigationState] into a list of decorated [NavEntry] instances
 * required for rendering by NavDisplay.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): List<NavEntry<NavKey>> {

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
}

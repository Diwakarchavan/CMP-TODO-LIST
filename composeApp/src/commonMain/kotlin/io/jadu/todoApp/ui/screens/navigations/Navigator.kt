package io.jadu.todoApp.ui.screens.navigations

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation commands and manages backstack history across different tabs.
 *
 * @property state The active [NavigationState] to update.
 */
class Navigator(val state: NavigationState) {
    /**
     * Navigates to a new destination. 
     * If the destination is a top-level route, it switches to that tab's backstack.
     *
     * @param route The destination [NavKey] to navigate to.
     */
    fun navigate(route: NavKey){
        if (route in state.backStacks.keys) {
            if (state.topLevelRoute != route) {
                if (route == state.startRoute) {
                    state.tabHistory = emptyList()
                } else {
                    // Ensure we don't have cycles in our backstack.
                    val updatedHistory = state.tabHistory.filter { it != route }.toMutableList()
                    
                    // Add the current tab we are leaving to the history
                    if (state.topLevelRoute != state.startRoute) {
                        updatedHistory.add(state.topLevelRoute)
                    }
                    state.tabHistory = updatedHistory
                }
                state.topLevelRoute = route
            } else {
                state.backStacks[route]?.let { stack ->
                    while (stack.size > 1) stack.removeLast()
                }
            }
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    /**
     * Navigates up in the backstack. 
     * If at the root of the current tab, it switches to the previously active tab.
     */
    fun goBack(){
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            if (state.tabHistory.isNotEmpty()) {
                val previousTab = state.tabHistory.last()
                state.tabHistory = state.tabHistory.dropLast(1)
                state.topLevelRoute = previousTab
            } else if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
            }
        } else {
            currentStack.removeLastOrNull()
        }
    }
}

package io.jadu.todoApp.ui.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute : NavKey {

    @Serializable
    data object CategorySphereScreen : NavRoute

    @Serializable
    @SerialName("OnboardingRoute")
    data object Onboarding : NavRoute

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object TaskScreen : NavRoute

    @Serializable
    data object AddProject : NavRoute

    @Serializable
    data object SettingsScreen : NavRoute

    @Serializable
    data object AboutUsScreen : NavRoute

    @Serializable
    data class EditTodo(val todoId: Long) : NavRoute

    @Serializable
    data object TestScreen : NavRoute
}

/**
 * Root level navigation graph routes
 */
@Serializable
sealed interface RootNavGraph : NavKey {

    @Serializable
    data object Onboarding : RootNavGraph

    @Serializable
    data object BottomNavBar : RootNavGraph
}
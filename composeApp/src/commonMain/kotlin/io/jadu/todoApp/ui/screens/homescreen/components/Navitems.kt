package io.jadu.todoApp.ui.screens.homescreen.components

import androidx.compose.runtime.Composable
import io.jadu.todoApp.ui.animatedBottomBar.models.IconSource
import io.jadu.todoApp.ui.animatedBottomBar.models.NavItem
import io.jadu.todoApp.ui.route.NavRoute
import org.jetbrains.compose.resources.stringResource
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.add
import todo_list.composeapp.generated.resources.briefcase
import todo_list.composeapp.generated.resources.calendar
import todo_list.composeapp.generated.resources.home
import todo_list.composeapp.generated.resources.navbar_add
import todo_list.composeapp.generated.resources.navbar_calendar
import todo_list.composeapp.generated.resources.navbar_home
import todo_list.composeapp.generated.resources.navbar_settings
import todo_list.composeapp.generated.resources.navbar_tasks
import todo_list.composeapp.generated.resources.user_octagon

@Composable
fun NavItems(){
val navItems = listOf(
    NavItem(
        icon = IconSource.Drawable(Res.drawable.home),
        label = stringResource(Res.string.navbar_home),
        route = NavRoute.Home
    ),
    NavItem(
        icon = IconSource.Drawable(Res.drawable.briefcase),
        label = stringResource(Res.string.navbar_tasks),
        route = NavRoute.TaskScreen
    ),
    NavItem(
        icon = IconSource.Drawable(Res.drawable.add),
        selectedIcon = IconSource.Drawable(Res.drawable.add),
        label = stringResource(Res.string.navbar_add),
        route = NavRoute.AddProject
    ),
    NavItem(
        icon = IconSource.Drawable(Res.drawable.calendar),
        label = stringResource(Res.string.navbar_calendar),
        route = NavRoute.SplashScreen
    ),
    NavItem(
        icon = IconSource.Drawable(Res.drawable.user_octagon),
        label = stringResource(Res.string.navbar_settings),
        route = NavRoute.SettingsPage
    )
)
}
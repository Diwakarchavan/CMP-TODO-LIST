package io.jadu.todoApp.ui.screens.homescreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.jadu.todoApp.ui.notification.NotificationViewModel
import io.jadu.todoApp.ui.notification.rememberActivity
import io.jadu.todoApp.ui.route.NavRoute
import io.jadu.todoApp.ui.theme.BodyLarge
import io.jadu.todoApp.ui.theme.Spacing
import io.jadu.todoApp.ui.theme.TodoColors
import io.jadu.todoApp.ui.uiutils.HSpacer
import io.jadu.todoApp.ui.uiutils.VSpacer
import io.jadu.todoApp.ui.viewModel.HomeScreenViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.in_progress2
import todo_list.composeapp.generated.resources.task_group
import todo_list.composeapp.generated.resources.no_task_groups

@Composable
fun HomePageContent(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinInject(),
    notificationVM: NotificationViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    val activity = rememberActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(Spacing.s4)
        ) {
            UserHeader(
                userProfile = uiState.userProfile,
                onNotificationClick = {
                    notificationVM.onEnableNotificationsClicked(activity)
                }
            )
            VSpacer(Spacing.s6)
            TaskProgressCard(
                progress = uiState.todayProgress,
                onViewTaskClick = {
                    navController.navigate(NavRoute.TaskScreen)
                }
            )
            if(uiState.inProgressTasks.isNotEmpty()) {
                VSpacer(Spacing.s4)
                SectionHeader(
                    title = stringResource(Res.string.in_progress2),
                    count = uiState.inProgressTasks.size
                )
            }
        }

        AnimatedVisibility(
            visible = uiState.inProgressTasks.isNotEmpty()
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = uiState.inProgressTasks,
                    key = { _, task -> task.id }
                ) { idx, task ->
                    if (idx == 0) HSpacer(Spacing.s4)
                    InProgressTaskCard(task)
                    if (idx == uiState.inProgressTasks.lastIndex) {
                        HSpacer(Spacing.s4)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(Spacing.s4)
        ) {
            SectionHeader(
                title = stringResource(Res.string.task_group),
                count = uiState.taskGroups.size
            )

            if (uiState.taskGroups.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.s12),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.no_task_groups),
                        style = BodyLarge().copy(
                            color = TodoColors.Secondary.color,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            } else {
                uiState.taskGroups.forEach { taskGroup ->
                    TaskGroupCard(taskGroup)
                }
            }
        }

        VSpacer(Spacing.s55)
    }
}



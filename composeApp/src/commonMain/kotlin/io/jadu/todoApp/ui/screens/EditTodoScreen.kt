package io.jadu.todoApp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import io.jadu.todoApp.data.model.TaskStatus
import io.jadu.todoApp.ui.components.CurvedButton
import io.jadu.todoApp.ui.components.CustomDialog
import io.jadu.todoApp.ui.components.CustomDialogConfig
import io.jadu.todoApp.ui.components.DatePickerDialog
import io.jadu.todoApp.ui.components.EditDetailCard
import io.jadu.todoApp.ui.components.SelectGroupBottomSheet
import io.jadu.todoApp.ui.components.SelectTaskStatusBottomSheet
import io.jadu.todoApp.ui.components.SelectionCard
import io.jadu.todoApp.ui.components.SelectionCardConfig
import io.jadu.todoApp.ui.components.TodoTopAppBar
import io.jadu.todoApp.ui.screens.homescreen.components.showSnackBar
import io.jadu.todoApp.ui.theme.BodyXLarge
import io.jadu.todoApp.ui.theme.Spacing
import io.jadu.todoApp.ui.theme.TodoColors
import io.jadu.todoApp.ui.uiutils.VSpacer
import io.jadu.todoApp.ui.utils.UiEvent
import io.jadu.todoApp.ui.viewModel.EditTodoEvent
import io.jadu.todoApp.ui.viewModel.EditTodoViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.calendar
import todo_list.composeapp.generated.resources.delete_rounded_icon
import todo_list.composeapp.generated.resources.delete_todo
import todo_list.composeapp.generated.resources.delete_todo2
import todo_list.composeapp.generated.resources.done
import todo_list.composeapp.generated.resources.edit_task
import todo_list.composeapp.generated.resources.in_progress
import todo_list.composeapp.generated.resources.project_description
import todo_list.composeapp.generated.resources.project_end_date
import todo_list.composeapp.generated.resources.project_sel_date
import todo_list.composeapp.generated.resources.project_st_date
import todo_list.composeapp.generated.resources.save_changes
import todo_list.composeapp.generated.resources.saving
import todo_list.composeapp.generated.resources.task_desc_plac
import todo_list.composeapp.generated.resources.task_group
import todo_list.composeapp.generated.resources.task_name
import todo_list.composeapp.generated.resources.task_status
import todo_list.composeapp.generated.resources.to_do

@Composable
@Preview
fun EditTodoScreen(
    onBack: () -> Unit,
    todoId: Long,
    viewModel: EditTodoViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var openTaskGroupSelection by remember { mutableStateOf(false) }
    var openTaskStatusSelection by remember { mutableStateOf(false) }
    var openStartDatePicker by remember { mutableStateOf(false) }
    var openEndDatePicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var shouldDeleteTodo by remember { mutableStateOf(false) }

    LaunchedEffect(todoId) {
        viewModel.onEvent(EditTodoEvent.LoadTodo(todoId))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when(event) {
                is UiEvent.OnSuccess -> {
                    showSnackBar(
                        message = event.message,
                        positiveMessage = true
                    )
                }
                is UiEvent.ShowError -> {
                    showSnackBar(
                        message = event.message,
                        positiveMessage = false
                    )
                }
                is UiEvent.OnIdle -> {}
                is UiEvent.OnLoading -> {
                    //show a loading screen
                }
            }
        }
    }

    // Navigate back when saved or deleted successfully
    LaunchedEffect(uiState.isSaved, uiState.isDeleted) {
        if (uiState.isSaved || uiState.isDeleted) {
            viewModel.onEvent(EditTodoEvent.OnUiReset)
            onBack()
        }
    }

    TodoBackgroundScreen {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TodoTopAppBar(
                title = stringResource(Res.string.edit_task),
                modifier = Modifier.systemBarsPadding(),
                onBack = onBack,
                actionImage = Res.drawable.delete_rounded_icon,
                onActionClick = {
                    shouldDeleteTodo = true
                }
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(Spacing.s4),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Task Group
                SelectionCard(
                    cardConfig = SelectionCardConfig(
                        title = stringResource(Res.string.task_group),
                        subtitle = uiState.selectedGroupCategory.displayName,
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(Spacing.s8)
                                    .background(
                                        color = uiState.selectedGroupCategory.color.copy(alpha = 0.15f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = uiState.selectedGroupCategory.icon,
                                    contentDescription = uiState.selectedGroupCategory.displayName,
                                    tint = uiState.selectedGroupCategory.color,
                                    modifier = Modifier.size(Spacing.s4)
                                )
                            }
                        }
                    )
                ) {
                    scope.launch {
                        openTaskGroupSelection = !openTaskGroupSelection
                    }
                }

                // Task Status
                SelectionCard(
                    cardConfig = SelectionCardConfig(
                        title = stringResource(Res.string.task_status),
                        subtitle = when (uiState.selectedStatus) {
                           TaskStatus.TO_DO -> stringResource(Res.string.to_do)
                            TaskStatus.IN_PROGRESS -> stringResource(Res.string.in_progress)
                            TaskStatus.DONE -> stringResource(Res.string.done)
                        },
                        leadingIcon = {
                            val statusColor = when (uiState.selectedStatus) {
                                TaskStatus.TO_DO -> TodoColors.Primary.color
                                TaskStatus.IN_PROGRESS -> TodoColors.Orange.color
                                TaskStatus.DONE -> TodoColors.Emerald.color
                            }
                            Box(
                                modifier = Modifier
                                    .size(Spacing.s8)
                                    .background(
                                        color = statusColor.copy(alpha = 0.15f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Spacing.s4)
                                        .background(
                                            color = statusColor,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    )
                ) {
                    scope.launch {
                        openTaskStatusSelection = !openTaskStatusSelection
                    }
                }

                // Task Name
                EditDetailCard(
                    value = uiState.title,
                    placeHolderText = stringResource(Res.string.task_name),
                    textStyle = BodyXLarge(),
                    onTextChange = {
                        viewModel.onEvent(EditTodoEvent.OnTitleChanged(it))
                    }
                )

                // Description
                EditDetailCard(
                    value = uiState.description,
                    title = stringResource(Res.string.project_description),
                    placeHolderText = stringResource(Res.string.task_desc_plac),
                    onTextChange = { alphabet ->
                        viewModel.onEvent(EditTodoEvent.OnDescriptionChanged(alphabet))
                    }
                )

                // Start date
                SelectionCard(
                    cardConfig = SelectionCardConfig(
                        title = stringResource(Res.string.project_st_date),
                        subtitle = uiState.startDate ?: stringResource(Res.string.project_sel_date),
                        leadingIcon = {
                            Image(
                                painter = painterResource(Res.drawable.calendar),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(
                                    color = TodoColors.Primary.color
                                )
                            )
                        }
                    )
                ) {
                    scope.launch {
                        openStartDatePicker = true
                    }
                }

                // End date
                SelectionCard(
                    cardConfig = SelectionCardConfig(
                        title = stringResource(Res.string.project_end_date),
                        subtitle = uiState.endDate ?: stringResource(Res.string.project_sel_date),
                        leadingIcon = {
                            Image(
                                painter = painterResource(Res.drawable.calendar),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(
                                    color = TodoColors.Primary.color
                                )
                            )
                        }
                    )
                ) {
                    scope.launch {
                        openEndDatePicker = true
                    }
                }

                VSpacer(Spacing.s12)

                // Action buttons
                CurvedButton(
                    modifier = Modifier,
                    isEnabled = !uiState.isLoading,
                    text = if (uiState.isLoading) stringResource(Res.string.saving) else stringResource(Res.string.save_changes)
                ) {
                    viewModel.onEvent(EditTodoEvent.OnSaveTodo)
                }
            }
        }

        if (openTaskGroupSelection)
            SelectGroupBottomSheet(
                onDismiss = {
                    openTaskGroupSelection = false
                },
                onCategorySelected = { category ->
                    viewModel.onEvent(EditTodoEvent.OnCategoryChanged(category))
                    openTaskGroupSelection = false
                }
            )

        if (openTaskStatusSelection)
            SelectTaskStatusBottomSheet(
                selectedStatus = uiState.selectedStatus,
                onDismiss = {
                    openTaskStatusSelection = false
                },
                onStatusSelected = { status ->
                    viewModel.onEvent(EditTodoEvent.OnStatusChanged(status))
                    openTaskStatusSelection = false
                }
            )

        if (openStartDatePicker)
            DatePickerDialog(
                onDateSelected = { dateString ->
                    viewModel.onEvent(EditTodoEvent.OnStartDateChanged(dateString))
                },
                onDismiss = {
                    openStartDatePicker = false
                }
            )

        if (openEndDatePicker)
            DatePickerDialog(
                onDateSelected = { dateString ->
                    viewModel.onEvent(EditTodoEvent.OnEndDateChanged(dateString))
                },
                onDismiss = {
                    openEndDatePicker = false
                }
            )

        DeletePermissionDialog(
            isOpen = shouldDeleteTodo,
            onDismiss = {
                shouldDeleteTodo = false
            },
            onConfirm = {
                viewModel.onEvent(EditTodoEvent.OnDeleteTodo)
            }
        )
    }
}

@Composable
fun DeletePermissionDialog(
    isOpen: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val config = CustomDialogConfig(
        title = stringResource(Res.string.delete_todo),
        subtitle = stringResource(Res.string.delete_todo2)
    )
    if (isOpen)
        CustomDialog(
            config = config,
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
}

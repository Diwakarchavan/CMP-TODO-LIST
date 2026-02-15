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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import io.jadu.todoApp.ui.components.CurvedButton
import io.jadu.todoApp.ui.components.DatePickerDialog
import io.jadu.todoApp.ui.components.EditDetailCard
import io.jadu.todoApp.ui.components.SelectGroupBottomSheet
import io.jadu.todoApp.ui.components.SelectionCard
import io.jadu.todoApp.ui.components.SelectionCardConfig
import io.jadu.todoApp.ui.components.TodoTopAppBar
import io.jadu.todoApp.ui.screens.homescreen.components.showSnackBar
import io.jadu.todoApp.ui.theme.BodyXLarge
import io.jadu.todoApp.ui.theme.Spacing
import io.jadu.todoApp.ui.theme.TodoColors
import io.jadu.todoApp.ui.uiutils.VSpacer
import io.jadu.todoApp.ui.utils.UiEvent
import io.jadu.todoApp.ui.viewModel.AddProjectUiEvent
import io.jadu.todoApp.ui.viewModel.AddProjectViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.add_project_task_group
import todo_list.composeapp.generated.resources.add_project_title
import todo_list.composeapp.generated.resources.calendar
import todo_list.composeapp.generated.resources.project_desc_plac
import todo_list.composeapp.generated.resources.project_description
import todo_list.composeapp.generated.resources.project_end_date
import todo_list.composeapp.generated.resources.project_name_plac
import todo_list.composeapp.generated.resources.project_sel_date
import todo_list.composeapp.generated.resources.project_st_date
import todo_list.composeapp.generated.resources.saving

@Composable
@Preview
fun AddProject(
    navController: NavHostController,
    viewModel: AddProjectViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var openTaskGroupSelection by remember { mutableStateOf(false) }
    var openStartDatePicker by remember { mutableStateOf(false) }
    var openEndDatePicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowError -> {
                    showSnackBar(
                        message = event.message,
                        positiveMessage = false
                    )
                }
                is UiEvent.OnSuccess -> {
                    showSnackBar(
                        message = event.message,
                        positiveMessage = true
                    )
                    viewModel.onEvent(AddProjectUiEvent.OnUiReset)
                    navController.navigateUp()
                }
                is UiEvent.OnLoading -> {
                    //showing loading
                }
                is UiEvent.OnIdle -> {}
            }
        }
    }

    TodoBackgroundScreen {

        // Don't give here, padding as topAppBar already have
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Appbar
            TodoTopAppBar(
                title = stringResource(Res.string.add_project_title),
                modifier = Modifier.systemBarsPadding(),
                navController = navController
            )

            // This column is used to give padding to components included
            Column(
                modifier = Modifier.fillMaxWidth().padding(Spacing.s4),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Add TaskGroup
                SelectionCard(
                    cardConfig = SelectionCardConfig(
                        title = stringResource(Res.string.add_project_task_group),
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

                // Add Project Name
                EditDetailCard(
                    value = uiState.title,
                    placeHolderText = stringResource(Res.string.project_name_plac),
                    textStyle = BodyXLarge(),
                    onTextChange = {
                        viewModel.onEvent(AddProjectUiEvent.OnTitleChanged(it))
                    }
                )

                // Add description
                EditDetailCard(
                    value = uiState.description,
                    title = stringResource(Res.string.project_description),
                    placeHolderText = stringResource(Res.string.project_desc_plac),
                    onTextChange = { alphabet ->
                        viewModel.onEvent(AddProjectUiEvent.OnDescriptionChanged(alphabet))
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

                // Take all the available Space below
                VSpacer(Spacing.s12)

                CurvedButton(
                    modifier = Modifier,
                    isEnabled = !uiState.isLoading,
                    text = if (uiState.isLoading) stringResource(Res.string.saving) else stringResource(Res.string.add_project_title)
                ) {
                    viewModel.onEvent(AddProjectUiEvent.OnSaveProjectUi)
                }
            }
        }

        if (openTaskGroupSelection)
            SelectGroupBottomSheet(
                onDismiss = {
                    openTaskGroupSelection = false
                },
                onCategorySelected = { category ->
                    viewModel.onEvent(AddProjectUiEvent.OnCategoryChanged(category))
                    openTaskGroupSelection = false
                }
            )

        if (openStartDatePicker)
            DatePickerDialog(
                onDateSelected = { dateString ->
                    viewModel.onEvent(AddProjectUiEvent.OnStartDateChanged(dateString))
                },
                onDismiss = {
                    openStartDatePicker = false
                }
            )

        if (openEndDatePicker)
            DatePickerDialog(
                onDateSelected = { dateString ->
                    viewModel.onEvent(AddProjectUiEvent.OnEndDateChanged(dateString))
                },
                onDismiss = {
                    openEndDatePicker = false
                }
            )
    }
}

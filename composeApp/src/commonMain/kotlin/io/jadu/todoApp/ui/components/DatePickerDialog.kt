package io.jadu.todoApp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import io.jadu.todoApp.ui.theme.Spacing
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.cancel
import todo_list.composeapp.generated.resources.ok
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun DatePickerDialog(
    initialDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    onDateSelected: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(initialDate) }


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Spacing.s6),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier) {
                MonthCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                    }
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text(stringResource(Res.string.cancel)) }
                    TextButton(onClick = {
                        // Format date as "dd MMM, yyyy" e.g., "17 Nov, 2025"
                        val monthName = selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
                        val formattedDate = "${selectedDate.day} $monthName, ${selectedDate.year}"
                        onDateSelected(formattedDate)
                        onDismiss()
                    }) { Text(stringResource(Res.string.ok)) }
                }
            }

        }
    }
}
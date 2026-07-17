package io.jadu.todoApp.ui.screens.homescreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.jadu.todoApp.data.model.TaskStatus
import io.jadu.todoApp.data.model.TodoItem
import io.jadu.todoApp.ui.theme.BodyNormal
import io.jadu.todoApp.ui.theme.BodySmall
import io.jadu.todoApp.ui.theme.BodyXSmall
import io.jadu.todoApp.ui.theme.Spacing
import io.jadu.todoApp.ui.theme.TodoColors
import io.jadu.todoApp.ui.uiutils.VSpacer
import org.jetbrains.compose.resources.stringResource
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.search_hint

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    val iconTint by animateColorAsState(
        targetValue = if (isFocused || query.isNotEmpty()) TodoColors.Primary.color else TodoColors.Secondary.color,
        animationSpec = tween(200),
        label = "searchIconTint"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) TodoColors.Primary.color else Color.Transparent,
        animationSpec = tween(200),
        label = "searchBorderColor"
    )

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) 6.dp else 2.dp,
                shape = RoundedCornerShape(Spacing.s9),
                ambientColor = TodoColors.Primary.color.copy(alpha = 0.08f),
                spotColor = TodoColors.Primary.color.copy(alpha = 0.12f)
            )
            .onFocusChanged { isFocused = it.isFocused },
        placeholder = {
            Text(
                text = stringResource(Res.string.search_hint),
                style = BodyNormal().copy(color = TodoColors.Secondary.color.copy(alpha = 0.6f)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = iconTint
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = {
                    onClear()
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = TodoColors.Secondary.color
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(Spacing.s9),
        textStyle = BodyNormal(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = TodoColors.LightPrimary.color,
            unfocusedContainerColor = TodoColors.LightPrimary.color.copy(alpha = 0.6f),
            cursorColor = TodoColors.Primary.color
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultItem(
    todo: TodoItem,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val statusColor = when (todo.status) {
        TaskStatus.DONE -> TodoColors.Emerald.color
        TaskStatus.IN_PROGRESS -> TodoColors.Orange.color
        TaskStatus.TO_DO -> TodoColors.Primary.color
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(Spacing.s4),
                ambientColor = TodoColors.Black.color.copy(alpha = 0.04f),
                spotColor = TodoColors.Black.color.copy(alpha = 0.08f)
            )
            .background(
                color = TodoColors.Light.color,
                shape = RoundedCornerShape(Spacing.s4)
            )
            .padding(Spacing.s4)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Status chip
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(Spacing.s2)
                    )
                    .padding(horizontal = Spacing.s2, vertical = Spacing.sHalf)
            ) {
                Text(
                    text = todo.status.name.replace('_', ' '),
                    style = BodyXSmall().copy(
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            VSpacer(Spacing.s2)

            // Highlighted title
            Text(
                text = highlightText(todo.title, searchQuery, TodoColors.LightPrimary.color),
                style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Highlighted description
            if (todo.description.isNotBlank()) {
                VSpacer(Spacing.s1)
                Text(
                    text = highlightText(todo.description, searchQuery, TodoColors.LightPrimary.color),
                    style = BodySmall().copy(color = TodoColors.Secondary.color),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Tags as small chips
            if (todo.tags.isNotEmpty()) {
                VSpacer(Spacing.s2)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.s1)) {
                    todo.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = TodoColors.LightPrimary.color,
                                    shape = RoundedCornerShape(Spacing.s2)
                                )
                                .padding(horizontal = Spacing.s2, vertical = Spacing.sHalf)
                        ) {
                            Text(
                                text = highlightText("#$tag", searchQuery, Color(0xFFD5C8FF)),
                                style = BodyXSmall().copy(color = TodoColors.Primary.color)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun highlightText(text: String, query: String, highlightColor: Color): AnnotatedString {
    if (query.isBlank()) return buildAnnotatedString { append(text) }
    val trimmedQuery = query.trim()
    return buildAnnotatedString {
        val lowerText = text.lowercase()
        val lowerQuery = trimmedQuery.lowercase()
        var cursor = 0
        while (cursor < text.length) {
            val matchIndex = lowerText.indexOf(lowerQuery, cursor)
            if (matchIndex == -1) {
                append(text.substring(cursor))
                break
            }
            append(text.substring(cursor, matchIndex))
            withStyle(SpanStyle(background = highlightColor, fontWeight = FontWeight.Bold)) {
                append(text.substring(matchIndex, matchIndex + trimmedQuery.length))
            }
            cursor = matchIndex + trimmedQuery.length
        }
    }
}

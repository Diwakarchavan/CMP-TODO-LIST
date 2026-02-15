package io.jadu.todoApp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import io.jadu.todoApp.ui.components.TodoTopAppBar
import io.jadu.todoApp.ui.theme.BodyLarge
import io.jadu.todoApp.ui.theme.BodyNormal
import io.jadu.todoApp.ui.theme.H2TextStyle
import io.jadu.todoApp.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import todo_list.composeapp.generated.resources.Res
import todo_list.composeapp.generated.resources.about_app
import todo_list.composeapp.generated.resources.about_cmp
import todo_list.composeapp.generated.resources.about_desc
import todo_list.composeapp.generated.resources.about_title


@Composable
fun AboutUsPage(
    navHostController: NavHostController
) {
    Scaffold(
        topBar = {
            TodoTopAppBar(
                modifier = Modifier.systemBarsPadding(),
                title = stringResource(Res.string.about_title),
                navController = navHostController
            )
        }
    ) { padding ->
        TodoBackgroundScreen {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(Spacing.s4),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.about_app),
                    style = H2TextStyle().copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Spacing.s3))

                Text(
                    text = "Version 1.0.0", //TODO: BUILD CONFIG VERSION
                    style = BodyLarge(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(Spacing.s5))

                Text(
                    text = stringResource(Res.string.about_desc),
                    style = BodyNormal(),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.s4))

                Text(
                    text = stringResource(Res.string.about_cmp),
                    style = BodyNormal(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


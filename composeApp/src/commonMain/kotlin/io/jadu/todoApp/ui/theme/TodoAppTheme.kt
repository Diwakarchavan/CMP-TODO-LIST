package io.jadu.todoApp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = TodoColors.DarkPrimary.color,
            onPrimary = TodoColors.White.color,
            primaryContainer = TodoColors.DarkCard.color,
            onPrimaryContainer = TodoColors.DarkOnSurface.color,
            secondary = TodoColors.DarkSecondary.color,
            onSecondary = TodoColors.White.color,
            background = TodoColors.DarkBackground.color,
            onBackground = TodoColors.DarkOnSurface.color,
            surface = TodoColors.DarkSurface.color,
            onSurface = TodoColors.DarkOnSurface.color,
            surfaceVariant = TodoColors.DarkCard.color,
            onSurfaceVariant = TodoColors.DarkOnSurface.color,
            outline = TodoColors.DarkOutline.color,
            error = TodoColors.Error.color,
            onError = TodoColors.White.color,
        )
    } else {
        lightColorScheme(
            primary = TodoColors.Primary.color,
            secondary = TodoColors.Secondary.color
        )
    }

    val fontFamily = manropeFamilyFont()
    val typography = Typography(
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colors.onSurface
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = colors.onSurface
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight(600),
            fontSize = 14.sp,
            color = colors.onSurface
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight(600),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = colors.onSurface
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight(700),
            fontSize = 16.sp,
            color = colors.onSurface
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = colors.onSurface
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            color = colors.onSurface
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight(700),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = colors.onSurface
        ),
    )

    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}
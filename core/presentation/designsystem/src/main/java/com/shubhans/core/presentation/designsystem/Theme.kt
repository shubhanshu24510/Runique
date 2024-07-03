package com.shubhans.core.presentation.designsystem

import android.R.id.primary
import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


val DarkColorScheme = darkColorScheme(
    primary = RuniqueGreen,
    background = RuniqueBlack,
    surface = RuniqueDarkGray,
    secondary = RuniqueWhite,
    tertiary = RuniqueWhite,
    onSurface = RuniqueDarkGray,
    primaryContainer = RuniqueGreen30,
    onBackground = RuniqueWhite,
    onPrimary = RuniqueBlack,
    error = RuniqueDarkRed,
    onErrorContainer = RuniqueDarkRed5,
    onSurfaceVariant = RuniqueGray,
)
@Composable
fun RuniqueTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
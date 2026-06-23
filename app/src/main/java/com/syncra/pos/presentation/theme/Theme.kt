package com.syncra.pos.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = BackgroundDark,
    secondary = SecondaryDark,
    onSecondary = ForegroundDark,
    tertiary = AccentOrange,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = ForegroundDark,
    surface = CardDark,
    onSurface = ForegroundDark,
    surfaceVariant = SecondaryDark,
    onSurfaceVariant = MutedForegroundDark,
    outline = BorderDark,
    error = DestructiveRed,
    onError = ForegroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreenDark,
    onPrimary = Color.White,
    secondary = SecondaryLight,
    onSecondary = ForegroundLight,
    tertiary = AccentOrange,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = ForegroundLight,
    surface = CardLight,
    onSurface = ForegroundLight,
    surfaceVariant = SecondaryLight,
    onSurfaceVariant = MutedForegroundLight,
    outline = BorderLight,
    error = DestructiveRed,
    onError = Color.White
)

@Composable
fun SyncraPosTheme(
    // We force the dark theme by default if we want to mimic the React template exactly, 
    // but here we support both gracefully, defaulting to system.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

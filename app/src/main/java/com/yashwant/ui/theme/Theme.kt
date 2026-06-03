package com.yashwant.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColors = darkColorScheme(
    primary = Orange,
    background = DarkBg,
    surface = DarkBg
)

private val LightColors = lightColorScheme(
    primary = Blue,
    background = LightBg,
    surface = LightBg
)

@Composable
fun SecondTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme)
            DarkColors
        else
            LightColors

    val view = LocalView.current

    if (!view.isInEditMode) {

        SideEffect {

            val window =
                (view.context as Activity).window

            // STATUS BAR COLOR
            window.statusBarColor =
                colors.background.toArgb()

            // NAVIGATION BAR COLOR
            window.navigationBarColor =
                colors.background.toArgb()

            val insetsController =
                WindowCompat.getInsetsController(
                    window,
                    view
                )

            // ICON COLORS
            insetsController.isAppearanceLightStatusBars =
                !darkTheme

            insetsController.isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
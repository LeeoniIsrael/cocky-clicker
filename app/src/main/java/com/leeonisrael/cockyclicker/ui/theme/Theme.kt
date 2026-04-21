package com.leeonisrael.cockyclicker.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CockyColorScheme = darkColorScheme(
    primary = Garnet,
    onPrimary = White,
    primaryContainer = DarkGarnet,
    onPrimaryContainer = Gold,
    secondary = Gold,
    onSecondary = NearBlack,
    secondaryContainer = DarkGold,
    onSecondaryContainer = NearBlack,
    tertiary = DarkGold,
    onTertiary = White,
    background = NearBlack,
    onBackground = White,
    surface = SurfaceDark,
    onSurface = White,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCCCCCC),
    error = ErrorRed,
    onError = White,
    outline = Color(0xFF555555),
)

@Composable
fun CockyClickerTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepGarnet.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = CockyColorScheme,
        typography = Typography,
        content = content
    )
}

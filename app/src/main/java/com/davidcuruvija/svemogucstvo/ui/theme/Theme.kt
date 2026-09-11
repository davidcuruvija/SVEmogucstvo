package com.davidcuruvija.svemogucstvo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = SlateGray,
    onSecondary = White,
    tertiary = AshGray,
    onTertiary = Black,
    background = White,
    onBackground = CharcoalText,
    surface = White,
    onSurface = CharcoalText,
    surfaceVariant = SurfaceOffWhite,
    onSurfaceVariant = SlateGray,
    outline = Divider,
    error = AlertRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    secondary = AshGray,
    onSecondary = Black,
    tertiary = SlateGray,
    onTertiary = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = AshGray,
    outline = Color(0xFF3D3D3D),
    error = AlertRed,
    onError = White
)

@Composable
fun SVEmogucstvoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

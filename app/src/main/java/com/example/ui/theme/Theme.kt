package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Navy850,
    onPrimary = Color.White,
    primaryContainer = Navy100,
    onPrimaryContainer = Navy900,
    secondary = GoldPrimary,
    onSecondary = Navy950,
    secondaryContainer = GoldPale,
    onSecondaryContainer = Gold900,
    tertiary = Gold700,
    onTertiary = Color.White,
    background = OffWhite,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = Navy50,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle.copy(alpha = 0.5f),
    error = CrimsonEmergency,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Navy950,
    primaryContainer = Navy800,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = Navy950,
    secondaryContainer = Navy850,
    onSecondaryContainer = GoldPale,
    tertiary = Navy500,
    onTertiary = Color.White,
    background = Navy950,
    onBackground = TextOnNavy,
    surface = SurfaceCardDark,
    onSurface = TextOnNavy,
    surfaceVariant = Navy900,
    onSurfaceVariant = TextOnNavySecondary,
    outline = BorderDark,
    outlineVariant = BorderDark.copy(alpha = 0.5f),
    error = CrimsonEmergency,
    onError = Color.White
)

@Composable
fun UmbrellaSafeTheme(
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

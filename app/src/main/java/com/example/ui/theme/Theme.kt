package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val border: Color,
    val borderMedium: Color,
    val goldPrimary: Color,
    val goldLight: Color,
    val goldPale: Color,
    val goldBorder: Color,
    val goldText: Color,
    val primaryButtonBg: Color,
    val primaryButtonText: Color,
    val topBarBg: Color
)

val LightAppColors = AppColors(
    isDark = false,
    background = OffWhite,
    surface = SurfaceCard,
    surfaceVariant = Navy50,
    surfaceElevated = Color.White,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textMuted = TextMuted,
    border = BorderSubtle,
    borderMedium = BorderMedium,
    goldPrimary = GoldPrimary,
    goldLight = GoldLight,
    goldPale = GoldPale,
    goldBorder = GoldBorder,
    goldText = Gold900,
    primaryButtonBg = Navy850,
    primaryButtonText = GoldLight,
    topBarBg = Navy900
)

val DarkAppColors = AppColors(
    isDark = true,
    background = Navy950,
    surface = SurfaceCardDark,
    surfaceVariant = Navy900,
    surfaceElevated = Navy800,
    textPrimary = TextOnNavy,
    textSecondary = TextOnNavySecondary,
    textMuted = Color(0xFF64748B),
    border = BorderDark,
    borderMedium = Color(0xFF2A4870),
    goldPrimary = GoldPrimary,
    goldLight = GoldLight,
    goldPale = Navy850,
    goldBorder = Color(0xFF6D521B),
    goldText = GoldLight,
    primaryButtonBg = GoldPrimary,
    primaryButtonText = Navy950,
    topBarBg = Navy950
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

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
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

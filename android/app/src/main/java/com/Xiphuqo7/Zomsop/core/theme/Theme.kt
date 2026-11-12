package com.Xiphuqo7.Zomsop.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PatrolColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = BgDeep,
    primaryContainer = AccentDeep,
    onPrimaryContainer = TextPrimary,
    secondary = AccentSecondary,
    onSecondary = BgDeep,
    secondaryContainer = SurfaceInactive,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentViolet,
    onTertiary = TextPrimary,
    background = BgDeep,
    onBackground = TextPrimary,
    surface = SurfaceGlass,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = StrokeAccent,
    error = DangerRed,
    onError = BgDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PatrolColorScheme,
        typography = AppTypography,
        content = content
    )
}

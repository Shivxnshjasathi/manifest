package com.zincstate.manifest.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.os.Build

private val ManifestDarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = DarkBackground,
    primaryContainer = DarkElevated,
    onPrimaryContainer = DarkTextPrimary,
    secondary = DarkTextSecondary,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = DarkTextTertiary,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
)

private val ManifestLightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = LightBackground,
    primaryContainer = LightElevated,
    onPrimaryContainer = LightTextPrimary,
    secondary = LightTextSecondary,
    onSecondary = LightBackground,
    secondaryContainer = LightSurface,
    onSecondaryContainer = LightTextPrimary,
    tertiary = LightTextTertiary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightElevated,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = LightBorder,
)

/**
 * Extended color scheme for app-specific colors beyond Material 3's palette.
 */
@Immutable
data class ManifestExtendedColors(
    val income: Color = Color.Unspecified,
    val expense: Color = Color.Unspecified,
    val transfer: Color = Color.Unspecified,
    val elevated: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val textTertiary: Color = Color.Unspecified,
)

val LocalManifestColors = staticCompositionLocalOf { ManifestExtendedColors() }

@Composable
fun ManifestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ManifestDarkColorScheme
        else -> ManifestLightColorScheme
    }

    // Derive extended colors from the active color scheme for uniform look
    val extendedColors = ManifestExtendedColors(
        income = colorScheme.primary,
        expense = colorScheme.onSurfaceVariant,
        transfer = colorScheme.secondary,
        elevated = colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = colorScheme.outline.copy(alpha = 0.2f),
        textSecondary = colorScheme.onSurfaceVariant,
        textTertiary = colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )

    CompositionLocalProvider(
        LocalManifestColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ManifestTypography,
            content = content
        )
    }
}

/**
 * Convenience accessor for extended colors from anywhere in the Compose tree.
 */
object ManifestThemeTokens {
    val colors: ManifestExtendedColors
        @Composable
        get() = LocalManifestColors.current
}

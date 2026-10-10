package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.model.AppThemePalette
import com.example.data.model.DarkModeOption

/**
 * Builds a tailored Material 3 ColorScheme based on the selected Theme Palette
 * and Dark/Light Mode setting.
 */
fun getAppColorScheme(
    palette: AppThemePalette,
    darkTheme: Boolean
): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = palette.primaryColor,
            onPrimary = Color.White,
            primaryContainer = palette.primaryColor.copy(alpha = 0.35f),
            onPrimaryContainer = Color.White,
            secondary = palette.secondaryColor,
            onSecondary = Color.White,
            secondaryContainer = palette.secondaryColor.copy(alpha = 0.35f),
            onSecondaryContainer = Color.White,
            tertiary = PersianGreen,
            background = DarkBackground,
            onBackground = Color(0xFFF1F5F9),
            surface = DarkSurface,
            onSurface = Color(0xFFF1F5F9),
            surfaceVariant = DarkSurfaceVariant,
            onSurfaceVariant = Color(0xFF94A3B8),
            error = AlertRed,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = palette.primaryColor,
            onPrimary = Color.White,
            primaryContainer = palette.containerColor,
            onPrimaryContainer = Color(0xFF0F172A),
            secondary = palette.secondaryColor,
            onSecondary = Color.White,
            secondaryContainer = palette.containerColor.copy(alpha = 0.6f),
            onSecondaryContainer = Color(0xFF0F172A),
            tertiary = PersianGreen,
            onTertiary = Color.White,
            background = BackgroundLight,
            onBackground = Color(0xFF0F172A),
            surface = SurfaceLight,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = SurfaceVariantLight,
            onSurfaceVariant = Color(0xFF334155),
            error = AlertRed,
            onError = Color.White
        )
    }
}

@Composable
fun MyApplicationTheme(
    palette: AppThemePalette = AppThemePalette.RAHA_EMERALD,
    darkModeOption: DarkModeOption = DarkModeOption.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (darkModeOption) {
        DarkModeOption.SYSTEM -> isSystemDark
        DarkModeOption.LIGHT -> false
        DarkModeOption.DARK -> true
    }

    val colorScheme = getAppColorScheme(palette, isDark)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = RahaGreen,
    onPrimary = Color.White,
    primaryContainer = RahaGreenContainer,
    onPrimaryContainer = OnRahaGreenContainer,
    secondary = RahaBlue,
    onSecondary = Color.White,
    secondaryContainer = RahaBlueContainer,
    onSecondaryContainer = OnRahaBlueContainer,
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

private val DarkColorScheme = darkColorScheme(
    primary = DarkTealPrimary,
    onPrimary = Color(0xFF003822),
    primaryContainer = Color(0xFF005232),
    onPrimaryContainer = Color(0xFF86EFAC),
    secondary = DarkRahaBlue,
    onSecondary = Color(0xFF001F3F),
    secondaryContainer = Color(0xFF0F3B68),
    onSecondaryContainer = Color(0xFFBFDBFE),
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our tailored brand palette for consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

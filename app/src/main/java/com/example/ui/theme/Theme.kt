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

private val DarkColorScheme = darkColorScheme(
    primary = ElectricPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF311B92),
    onPrimaryContainer = Color(0xFFEDE7F6),
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF880E4F),
    tertiary = SunriseGold,
    background = DeepMidnight,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE7F6),
    secondary = Color(0xFFC2185B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFCE4EC),
    tertiary = Color(0xFFFF6D00),
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep our vibrant energetic dance branding consistent
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme // High contrast energetic dark looks best for alarm & dance grooves

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

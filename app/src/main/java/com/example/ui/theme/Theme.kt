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

private val DarkColorScheme =
  darkColorScheme(
    primary = MinervaBlue,
    onPrimary = MinervaWhite,
    secondary = MinervaBlueLight,
    onSecondary = MinervaNavy,
    tertiary = MinervaGold,
    onTertiary = MinervaWhite,
    background = Color(0xFF0F172A),
    surface = MinervaCardBackgroundDark,
    onBackground = MinervaOffWhite,
    onSurface = MinervaOffWhite,
    error = MinervaError,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = MinervaBlue,
    onPrimary = MinervaWhite,
    secondary = MinervaNavy,
    onSecondary = MinervaWhite,
    tertiary = MinervaGold,
    onTertiary = MinervaWhite,
    background = MinervaOffWhite,
    surface = MinervaWhite,
    onBackground = MinervaGrayDark,
    onSurface = MinervaGrayDark,
    primaryContainer = MinervaBlueLight,
    onPrimaryContainer = MinervaBlueDark,
    error = MinervaError,
  )

@Composable
fun MinervaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Custom colors for our brand identity (disable dynamic colors by default to retain Minerva brand)
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

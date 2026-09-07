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

private val CleanMinimalismColorScheme = lightColorScheme(
  primary = CleanGreen,
  onPrimary = Color.White,
  primaryContainer = PastelGreenBg,
  onPrimaryContainer = PastelGreenText,
  secondary = CleanTextSecondary,
  onSecondary = Color.White,
  secondaryContainer = PastelBlueBg,
  onSecondaryContainer = PastelBlueText,
  background = CleanBg,
  onBackground = CleanTextPrimary,
  surface = CleanSurface,
  onSurface = CleanTextPrimary,
  surfaceVariant = CleanSurfaceVariant,
  onSurfaceVariant = CleanTextSecondary,
  outline = CleanBorder,
)

private val DarkMinimalismColorScheme = darkColorScheme(
  primary = CleanGreen,
  onPrimary = Color.Black,
  primaryContainer = PastelGreenBg.copy(alpha = 0.2f),
  onPrimaryContainer = CleanGreen,
  secondary = PureBlackTextSecondary,
  onSecondary = Color.Black,
  secondaryContainer = PastelBlueBg.copy(alpha = 0.2f),
  onSecondaryContainer = PastelBlueText,
  background = PureBlackBg,
  onBackground = PureBlackTextPrimary,
  surface = PureBlackSurface,
  onSurface = PureBlackTextPrimary,
  surfaceVariant = PureBlackSurfaceVariant,
  onSurfaceVariant = PureBlackTextSecondary,
  outline = PureBlackBorder,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    darkTheme -> DarkMinimalismColorScheme // Always honor Pure Black AMOLED theme when enabled by user
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      dynamicLightColorScheme(context)
    }
    else -> CleanMinimalismColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

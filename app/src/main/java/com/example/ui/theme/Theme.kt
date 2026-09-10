package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CleanMinimalismColorScheme = lightColorScheme(
  primary = CleanGreen,
  onPrimary = Color.White,
  primaryContainer = PastelGreenBg,
  onPrimaryContainer = PastelGreenText,
  secondary = LightCleanTextSecondary,
  onSecondary = Color.White,
  secondaryContainer = PastelBlueBg,
  onSecondaryContainer = PastelBlueText,
  background = LightCleanBg,
  onBackground = LightCleanTextPrimary,
  surface = LightCleanSurface,
  onSurface = LightCleanTextPrimary,
  surfaceVariant = LightCleanSurfaceVariant,
  onSurfaceVariant = LightCleanTextSecondary,
  outline = LightCleanBorder,
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

data class LauncherColors(
    val bg: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accentGreen: Color,
    val isDark: Boolean
)

val LightLauncherColors = LauncherColors(
    bg = LightCleanBg,
    surface = LightCleanSurface,
    surfaceVariant = LightCleanSurfaceVariant,
    border = LightCleanBorder,
    textPrimary = LightCleanTextPrimary,
    textSecondary = LightCleanTextSecondary,
    textMuted = LightCleanTextMuted,
    accentGreen = CleanGreen,
    isDark = false
)

val DarkLauncherColors = LauncherColors(
    bg = PureBlackBg,
    surface = PureBlackSurface,
    surfaceVariant = PureBlackSurfaceVariant,
    border = PureBlackBorder,
    textPrimary = PureBlackTextPrimary,
    textSecondary = PureBlackTextSecondary,
    textMuted = PureBlackTextMuted,
    accentGreen = CleanGreen,
    isDark = true
)

val LocalLauncherColors = androidx.compose.runtime.staticCompositionLocalOf { LightLauncherColors }

object AppTheme {
    val colors: LauncherColors
        @Composable
        @androidx.compose.runtime.ReadOnlyComposable
        get() = LocalLauncherColors.current
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val context = LocalContext.current
  val isDynamicSupported = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  val colorScheme = when {
    isDynamicSupported && darkTheme -> dynamicDarkColorScheme(context)
    isDynamicSupported && !darkTheme -> dynamicLightColorScheme(context)
    darkTheme -> DarkMinimalismColorScheme
    else -> CleanMinimalismColorScheme
  }

  val launcherColors = if (isDynamicSupported) {
    LauncherColors(
      bg = colorScheme.background,
      surface = colorScheme.surface,
      surfaceVariant = colorScheme.surfaceVariant,
      border = colorScheme.outlineVariant.copy(alpha = 0.45f),
      textPrimary = colorScheme.onSurface,
      textSecondary = colorScheme.onSurfaceVariant,
      textMuted = colorScheme.outline,
      accentGreen = colorScheme.primary,
      isDark = darkTheme
    )
  } else if (darkTheme) {
    DarkLauncherColors
  } else {
    LightLauncherColors
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.isAppearanceLightStatusBars = !darkTheme
        insetsController.isAppearanceLightNavigationBars = !darkTheme
      }
    }
  }

  androidx.compose.runtime.CompositionLocalProvider(
    LocalLauncherColors provides launcherColors
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}

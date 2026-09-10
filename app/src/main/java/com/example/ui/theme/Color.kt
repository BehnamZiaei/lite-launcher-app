package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// Clean Minimalism Palette - dynamically bound to active AppTheme with dark/light mode support
val CleanBg: Color
    @Composable
    get() = AppTheme.colors.bg

val CleanSurface: Color
    @Composable
    get() = AppTheme.colors.surface

val CleanSurfaceVariant: Color
    @Composable
    get() = AppTheme.colors.surfaceVariant

val CleanBorder: Color
    @Composable
    get() = AppTheme.colors.border

val CleanTextPrimary: Color
    @Composable
    get() = AppTheme.colors.textPrimary

val CleanTextSecondary: Color
    @Composable
    get() = AppTheme.colors.textSecondary

val CleanTextMuted: Color
    @Composable
    get() = AppTheme.colors.textMuted

val CleanGreen = Color(0xFF34A853)

val CleanGreenDark: Color
    @Composable
    get() = if (AppTheme.colors.isDark) Color(0xFF81C784) else Color(0xFF072711)

// Light static defaults for theme definition
val LightCleanBg = Color(0xFFF7F9FC)
val LightCleanSurface = Color(0xFFFFFFFF)
val LightCleanSurfaceVariant = Color(0xFFE2E7EE)
val LightCleanBorder = Color(0xFFE1E3E8)
val LightCleanTextPrimary = Color(0xFF1C1B1F)
val LightCleanTextSecondary = Color(0xFF44474E)
val LightCleanTextMuted = Color(0xFF74777F)

// Pastel accent badges from Clean Minimalism design
val PastelBlueBg = Color(0xFFD3E3FD)
val PastelBlueText = Color(0xFF041E49)
val PastelPurpleBg = Color(0xFFE8DEF8)
val PastelPurpleText = Color(0xFF21005D)
val PastelGreenBg = Color(0xFFC4EED0)
val PastelGreenText = Color(0xFF072711)
val PastelPinkBg = Color(0xFFFAD8FD)
val PastelPinkText = Color(0xFF31111D)
val PastelAmberBg = Color(0xFFFEF7DA)
val PastelAmberText = Color(0xFF4F4200)
val PastelCoralBg = Color(0xFFFFDBCF)
val PastelCoralText = Color(0xFF3E1C00)

// Backward compatibility & alerts
val DangerRed = Color(0xFFBA1A1A)
val AmberWarning = Color(0xFFE65100)

// Dark / Pure Black (OLED) palette
val PureBlackBg = Color(0xFF000000)
val PureBlackSurface = Color(0xFF0A0A0A)
val PureBlackSurfaceVariant = Color(0xFF141414)
val PureBlackBorder = Color(0xFF222222)
val PureBlackTextPrimary = Color(0xFFF5F5F5)
val PureBlackTextSecondary = Color(0xFFAAAAAA)
val PureBlackTextMuted = Color(0xFF666666)

// Nothing OS Signature Palette
val NothingRed = Color(0xFFD71921)
val NothingDarkRed = Color(0xFF8B0000)
val NothingDotInactive = Color(0xFF222222)
val NothingDotActive = Color(0xFFEEEEEE)
val NothingGlassBorder = Color(0x33FFFFFF)
val NothingDarkGlass = Color(0x1AFFFFFF)

// Dark fallback palette
val DarkBg = Color(0xFF000000)
val DarkSurface = Color(0xFF0A0A0A)
val DarkSurfaceVariant = Color(0xFF141414)
val DarkBorder = Color(0xFF222222)
val DarkTextPrimary = Color(0xFFF5F5F5)
val DarkTextSecondary = Color(0xFFAAAAAA)
val DarkTextMuted = Color(0xFF666666)

// Google Pixel Signature Palette
val GoogleBlue = Color(0xFF4285F4)
val GoogleRed = Color(0xFFEA4335)
val GoogleYellow = Color(0xFFFBBC05)
val GoogleGreen = Color(0xFF34A853)
val PixelSearchPillLight = Color(0xFFEEF2F6)
val PixelSearchPillDark = Color(0xFF1E1F22)
val PixelDockBgLight = Color(0x14000000)
val PixelDockBgDark = Color(0x26FFFFFF)
val PixelChipBgLight = Color(0xFFE8ECEF)
val PixelChipBgDark = Color(0xFF282A2E)

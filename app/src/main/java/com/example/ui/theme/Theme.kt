package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val PremiumLightColorScheme = lightColorScheme(
  primary = TowfikPrimaryBlue,
  onPrimary = Color.White,
  primaryContainer = BrandSky100,
  onPrimaryContainer = BrandNavy900,
  secondary = TowfikSecondaryBlue,
  onSecondary = Color.White,
  secondaryContainer = BrandSky50,
  onSecondaryContainer = BrandNavy900,
  tertiary = PremiumGold500,
  onTertiary = Color.White,
  tertiaryContainer = PremiumGold100,
  onTertiaryContainer = Color(0xFF78350F),
  error = PremiumRose600,
  onError = Color.White,
  errorContainer = PremiumRose100,
  onErrorContainer = PremiumRose700,
  background = PremiumBg,
  onBackground = PremiumInk900,
  surface = PremiumSurface,
  onSurface = PremiumInk900,
  surfaceVariant = PremiumSlate100,
  onSurfaceVariant = PremiumSlate600,
  surfaceContainerLowest = Color.White,
  surfaceContainerLow = Color(0xFFF8FAFD),
  surfaceContainer = Color(0xFFF1F4F9),
  surfaceContainerHigh = Color(0xFFE9EEF5),
  surfaceContainerHighest = Color(0xFFDEE5EF),
  outline = PremiumSlate200,
  outlineVariant = PremiumCardBorder,
  scrim = BrandNavy950,
)

private val PremiumDarkColorScheme = darkColorScheme(
  primary = BrandSky400,
  onPrimary = BrandNavy950,
  primaryContainer = DarkBlueTint,
  onPrimaryContainer = BrandSky100,
  secondary = BrandSky400,
  onSecondary = BrandNavy950,
  secondaryContainer = DarkBlueTint,
  onSecondaryContainer = BrandSky100,
  tertiary = PremiumGold400,
  onTertiary = BrandNavy950,
  tertiaryContainer = DarkGoldTint,
  onTertiaryContainer = PremiumGold100,
  error = Color(0xFFFF8A9B),
  onError = BrandNavy950,
  errorContainer = DarkRoseTint,
  onErrorContainer = PremiumRose100,
  background = PremiumDarkBg,
  onBackground = PremiumDarkTextPrimary,
  surface = PremiumDarkSurface,
  onSurface = PremiumDarkTextPrimary,
  surfaceVariant = PremiumDarkCard,
  onSurfaceVariant = PremiumDarkTextSecondary,
  surfaceContainerLowest = Color(0xFF070D1A),
  surfaceContainerLow = PremiumDarkSurface,
  surfaceContainer = PremiumDarkCard,
  surfaceContainerHigh = PremiumDarkCardElevated,
  surfaceContainerHighest = Color(0xFF22345A),
  outline = PremiumDarkBorder,
  outlineVariant = PremiumDarkDivider,
  scrim = Color.Black,
)

private val PremiumShapes = Shapes(
  extraSmall = RoundedCornerShape(8.dp),
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(16.dp),
  large = RoundedCornerShape(22.dp),
  extraLarge = RoundedCornerShape(30.dp),
)

@Composable
fun TowfikTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) PremiumDarkColorScheme else PremiumLightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      // Transparent premium edge-to-edge system bars.
      window.statusBarColor = Color.Transparent.toArgb()
      window.navigationBarColor = Color.Transparent.toArgb()
      val insetsController = WindowCompat.getInsetsController(window, view)
      insetsController.isAppearanceLightStatusBars = !darkTheme
      insetsController.isAppearanceLightNavigationBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = PremiumShapes,
    content = content,
  )
}

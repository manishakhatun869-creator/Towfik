package com.example.ui.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Theme-aware premium colors (light + dark ready)
// ---------------------------------------------------------------------------
@Composable
fun premiumBackground(): Color =
  if (isSystemInDarkTheme()) PremiumDarkBg else PremiumBg

@Composable
fun premiumSurface(): Color =
  if (isSystemInDarkTheme()) PremiumDarkSurface else PremiumSurface

@Composable
fun premiumCard(): Color =
  if (isSystemInDarkTheme()) PremiumDarkCard else PremiumSurface

@Composable
fun premiumCardElevated(): Color =
  if (isSystemInDarkTheme()) PremiumDarkCardElevated else PremiumSurface

@Composable
fun premiumTextPrimary(): Color =
  if (isSystemInDarkTheme()) PremiumDarkTextPrimary else PremiumInk900

@Composable
fun premiumTextSecondary(): Color =
  if (isSystemInDarkTheme()) PremiumDarkTextSecondary else PremiumSlate600

@Composable
fun premiumTextTertiary(): Color =
  if (isSystemInDarkTheme()) PremiumDarkTextTertiary else PremiumSlate500

@Composable
fun premiumBorder(): Color =
  if (isSystemInDarkTheme()) PremiumDarkBorder else PremiumCardBorder

@Composable
fun premiumDivider(): Color =
  if (isSystemInDarkTheme()) PremiumDarkDivider else PremiumDivider

@Composable
fun premiumMutedChipBg(): Color =
  if (isSystemInDarkTheme()) PremiumDarkCardElevated else PremiumSlate100

@Composable
fun premiumQuestionBg(): Color =
  if (isSystemInDarkTheme()) QuestionBoxBgDark else QuestionBoxBg

@Composable
fun premiumQuestionBorder(): Color =
  if (isSystemInDarkTheme()) QuestionBoxBorderDark else QuestionBoxBorder

@Composable
fun premiumQuestionTitle(): Color =
  if (isSystemInDarkTheme()) QuestionTextColorDark else QuestionTextColor

@Composable
fun premiumQuestionBody(): Color =
  if (isSystemInDarkTheme()) QuestionBodyColorDark else QuestionBodyColor

@Composable
fun premiumAnswerBg(): Color =
  if (isSystemInDarkTheme()) AnswerBoxBgDark else AnswerBoxBg

@Composable
fun premiumAnswerBorder(): Color =
  if (isSystemInDarkTheme()) AnswerBoxBorderDark else AnswerBoxBorder

@Composable
fun premiumAnswerTitle(): Color =
  if (isSystemInDarkTheme()) AnswerTextColorDark else AnswerTextColor

@Composable
fun premiumAnswerBody(): Color =
  if (isSystemInDarkTheme()) AnswerBodyColorDark else AnswerBodyColor

// ---------------------------------------------------------------------------
// Premium gradient brushes
// ---------------------------------------------------------------------------
val BrandGradient: Brush = Brush.linearGradient(
  colors = listOf(BrandNavy900, BrandRoyal700, BrandRoyal600, BrandBlue500),
)

val HeroGradient: Brush = Brush.verticalGradient(
  colors = listOf(BrandNavy950, BrandNavy900, BrandRoyal700, TowfikPrimaryBlue),
)

val HeaderGradient: Brush = Brush.linearGradient(
  colors = listOf(BrandNavy900, TowfikPrimaryBlue, BrandBlue500),
)

val GoldGradient: Brush = Brush.linearGradient(
  colors = listOf(PremiumGold600, PremiumGold500, PremiumGold400),
)

val SuccessGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFF15803D), Color(0xFF22C55E)),
)

val RoseGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFFBE123C), Color(0xFFF43F5E)),
)

val VioletGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFF6D28D9), Color(0xFFA78BFA)),
)

val TealGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFF0F766E), Color(0xFF2DD4BF)),
)

val SkyGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFF0369A1), Color(0xFF38BDF8)),
)

val IndigoGradient: Brush = Brush.linearGradient(
  colors = listOf(Color(0xFF3730A3), Color(0xFF818CF8)),
)

// ---------------------------------------------------------------------------
// Press-scale interaction (premium tactile feedback)
// ---------------------------------------------------------------------------
@Composable
fun rememberPressInteraction(): MutableInteractionSource =
  remember { MutableInteractionSource() }

fun Modifier.pressScale(
  interactionSource: MutableInteractionSource,
  pressedScale: Float = 0.965f
): Modifier = composed {
  val pressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (pressed) pressedScale else 1f,
    label = "pressScale",
  )
  this.graphicsLayer(scaleX = scale, scaleY = scale)
}

// Soft premium shadow for cards / buttons.
fun Modifier.premiumShadow(
  elevation: Dp = 10.dp,
  shape: Shape = RoundedCornerShape(20.dp),
  alpha: Float = 0.10f
): Modifier = composed {
  this.shadow(elevation = elevation, shape = shape, clip = false, ambientColor = BrandNavy900.copy(alpha = alpha), spotColor = BrandNavy900.copy(alpha = alpha))
}

// ---------------------------------------------------------------------------
// PremiumCard — elevated white/dark card with hairline border + soft shadow
// ---------------------------------------------------------------------------
@Composable
fun PremiumCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 20.dp,
  backgroundColor: Color = premiumCard(),
  borderColor: Color = premiumBorder(),
  showBorder: Boolean = true,
  contentPadding: PaddingValues = PaddingValues(16.dp),
  onClick: (() -> Unit)? = null,
  content: @Composable () -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  val interaction = rememberPressInteraction()
  val clickableModifier = if (onClick != null) {
    Modifier
      .pressScale(interaction)
      .clickable(interactionSource = interaction, indication = null, onClick = onClick)
  } else {
    Modifier
  }
  Card(
    modifier = modifier
      .premiumShadow(elevation = 8.dp, shape = shape)
      .then(clickableModifier),
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = if (showBorder) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null,
  ) {
    Box(modifier = Modifier.fillMaxWidth().padding(contentPadding)) {
      content()
    }
  }
}

// ---------------------------------------------------------------------------
// GradientIconTile — glossy gradient icon tile used across all sections
// ---------------------------------------------------------------------------
@Composable
fun GradientIconTile(
  icon: ImageVector,
  gradient: Brush,
  contentDescription: String? = null,
  size: Dp = 44.dp,
  cornerRadius: Dp = 13.dp,
  iconSize: Dp = 22.dp,
  iconTint: Color = Color.White
) {
  Box(
    modifier = Modifier
      .size(size)
      .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(cornerRadius), alpha = 0.22f)
      .clip(RoundedCornerShape(cornerRadius))
      .background(gradient),
    contentAlignment = Alignment.Center,
  ) {
    // Glass highlight
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(size / 2)
        .align(Alignment.TopCenter)
        .background(
          Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
          ),
        ),
    )
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = iconTint,
      modifier = Modifier.size(iconSize),
    )
  }
}

// ---------------------------------------------------------------------------
// SectionHeader — premium section title row with optional action
// ---------------------------------------------------------------------------
@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  actionLabel: String? = null,
  onActionClick: (() -> Unit)? = null,
  leading: @Composable (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.weight(1f, fill = false),
    ) {
      leading?.invoke()
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = premiumTextPrimary(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (subtitle != null) {
          Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = premiumTextTertiary(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
    if (actionLabel != null && onActionClick != null) {
      TextButton(onClick = onActionClick, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
        Text(
          text = actionLabel,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = if (isSystemInDarkTheme()) BrandSky400 else TowfikPrimaryBlue,
        )
      }
    }
  }
}

// ---------------------------------------------------------------------------
// PremiumGradientButton — full-width gradient CTA with gloss + shadow
// ---------------------------------------------------------------------------
@Composable
fun PremiumGradientButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  gradient: Brush = BrandGradient,
  height: Dp = 50.dp,
  cornerRadius: Dp = 14.dp,
  fontSize: TextUnit = 14.sp,
  enabled: Boolean = true
) {
  val interaction = rememberPressInteraction()
  val shape = RoundedCornerShape(cornerRadius)
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
      .premiumShadow(elevation = if (enabled) 8.dp else 0.dp, shape = shape, alpha = 0.25f)
      .pressScale(interaction)
      .clip(shape)
      .background(if (enabled) gradient else Brush.linearGradient(listOf(Color(0xFF94A3B8), Color(0xFFCBD5E1))))
      .clickable(
        interactionSource = interaction,
        indication = null,
        enabled = enabled,
        onClick = onClick,
      ),
    contentAlignment = Alignment.Center,
  ) {
    // Top gloss
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(height / 2)
        .align(Alignment.TopCenter)
        .background(
          Brush.verticalGradient(colors = listOf(Color.White.copy(alpha = 0.18f), Color.Transparent)),
        ),
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      if (icon != null) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
      }
      Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

// ---------------------------------------------------------------------------
// PremiumEmptyState — polished empty card used in every section
// ---------------------------------------------------------------------------
@Composable
fun PremiumEmptyState(
  icon: ImageVector,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier,
  iconGradient: Brush = BrandGradient,
  actionLabel: String? = null,
  onActionClick: (() -> Unit)? = null
) {
  PremiumCard(modifier = modifier.fillMaxWidth(), contentPadding = PaddingValues(26.dp)) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      GradientIconTile(
        icon = icon,
        gradient = iconGradient,
        size = 58.dp,
        cornerRadius = 18.dp,
        iconSize = 28.dp,
      )
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = premiumTextPrimary(),
        textAlign = TextAlign.Center,
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = premiumTextTertiary(),
        textAlign = TextAlign.Center,
        lineHeight = 19.sp,
      )
      if (actionLabel != null && onActionClick != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Button(
          onClick = onActionClick,
          colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
          shape = RoundedCornerShape(10.dp),
        ) {
          Text(actionLabel, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
        }
      }
    }
  }
}

// ---------------------------------------------------------------------------
// CountBadge — small premium pill showing counts / statuses
// ---------------------------------------------------------------------------
@Composable
fun CountBadge(
  text: String,
  containerColor: Color = PremiumSuccess100,
  contentColor: Color = PremiumSuccess700,
  modifier: Modifier = Modifier
) {
  val dark = isSystemInDarkTheme()
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .background(if (dark) DarkGreenTint else containerColor)
      .border(1.dp, if (dark) DarkGreenTintBorder else containerColor, RoundedCornerShape(20.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp),
  ) {
    Text(
      text = text,
      fontSize = 11.5.sp,
      fontWeight = FontWeight.Bold,
      color = if (dark) Color(0xFF86EFAC) else contentColor,
    )
  }
}

// ---------------------------------------------------------------------------
// DecorativeHeaderBackground — gradient + glass circles for screen headers
// ---------------------------------------------------------------------------
@Composable
fun DecorativeHeaderBackground(
  gradient: Brush = HeaderGradient,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.background(gradient)) {
    // Large soft circle — top right
    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .offset(x = 60.dp, y = (-70).dp)
        .size(190.dp)
        .background(Color.White.copy(alpha = 0.08f), CircleShape),
    )
    // Medium glass circle — center right
    Box(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .offset(x = 34.dp)
        .size(120.dp)
        .background(Color.White.copy(alpha = 0.06f), CircleShape),
    )
    // Small glass circle — bottom left
    Box(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .offset(x = 26.dp, y = 46.dp)
        .size(110.dp)
        .background(Color.White.copy(alpha = 0.06f), CircleShape),
    )
    // Tiny gold dot accent
    Box(
      modifier = Modifier
        .align(Alignment.TopStart)
        .offset(x = 148.dp, y = 18.dp)
        .size(9.dp)
        .background(PremiumGold400.copy(alpha = 0.9f), CircleShape),
    )
  }
}

// ---------------------------------------------------------------------------
// PremiumScreenHeader — gradient rounded header with icon + title + subtitle
// ---------------------------------------------------------------------------
@Composable
fun PremiumScreenHeader(
  title: String,
  subtitle: String,
  icon: ImageVector,
  modifier: Modifier = Modifier,
  gradient: Brush = HeaderGradient,
  trailing: @Composable (() -> Unit)? = null
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
  ) {
    DecorativeHeaderBackground(gradient = gradient, modifier = Modifier.matchParentSize())
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp, vertical = 22.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White.copy(alpha = 0.16f))
          .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(26.dp))
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White,
          letterSpacing = (-0.25).sp,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = subtitle,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFFD6E4FF),
          lineHeight = 17.sp,
        )
      }
      trailing?.invoke()
    }
  }
}

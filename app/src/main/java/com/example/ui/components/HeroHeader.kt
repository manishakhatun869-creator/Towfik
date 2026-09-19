package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandNavy950
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.HeroGradient
import com.example.ui.theme.PremiumGold400
import com.example.ui.theme.premiumShadow

@Composable
fun HeroHeader(
  streakCount: Int = 4,
  xpCount: Int = 180,
  onNotificationClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .premiumShadow(elevation = 14.dp, shape = RoundedCornerShape(26.dp), alpha = 0.28f)
      .testTag("hero_header_card"),
    shape = RoundedCornerShape(26.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(HeroGradient),
    ) {
      // Study banner backdrop
      Image(
        painter = painterResource(id = R.drawable.hero_study_banner),
        contentDescription = "Study Banner",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(196.dp),
        alpha = 0.22f,
      )
      // Soft gradient veil for legibility
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(196.dp)
          .background(
            Brush.verticalGradient(
              colors = listOf(
                BrandNavy950.copy(alpha = 0.45f),
                Color.Transparent,
                BrandNavy950.copy(alpha = 0.55f),
              ),
            ),
          ),
      )
      // Decorative glass circles
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .offset(x = 56.dp, y = (-64).dp)
          .size(170.dp)
          .background(Color.White.copy(alpha = 0.08f), CircleShape),
      )
      Box(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset(x = (-36).dp, y = 44.dp)
          .size(120.dp)
          .background(PremiumGold400.copy(alpha = 0.14f), CircleShape),
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp),
      ) {
        // Top Row: Logo + App Title + Bell icon
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f, fill = false),
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .premiumShadow(elevation = 8.dp, shape = RoundedCornerShape(15.dp), alpha = 0.35f)
                .clip(RoundedCornerShape(15.dp))
                .border(1.5.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(15.dp)),
            ) {
              Image(
                painter = painterResource(id = R.drawable.ic_launcher_logo),
                contentDescription = "Towfik Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
              )
            }

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "TOWFIK EXCLUSIVE",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  letterSpacing = 0.4.sp,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(GoldGradient)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                  ) {
                    Icon(
                      imageVector = Icons.Default.AutoAwesome,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(10.dp),
                    )
                    Text(
                      text = "PRO",
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Black,
                      color = Color.White,
                      letterSpacing = 0.8.sp,
                    )
                  }
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Madhyamik • Class 9–12 • Notes & Alerts",
                fontSize = 11.5.sp,
                color = Color(0xFFCBD8F2),
                fontWeight = FontWeight.Medium,
              )
            }
          }

          Box {
            IconButton(
              onClick = onNotificationClick,
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.14f))
                .border(1.dp, Color.White.copy(alpha = 0.28f), CircleShape)
                .testTag("notification_bell_btn"),
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Alerts",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
              )
            }
            // Live notification dot
            Box(
              modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-2).dp, y = 2.dp)
                .size(11.dp)
                .clip(CircleShape)
                .background(PremiumGold400)
                .border(2.dp, BrandNavy950, CircleShape),
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bottom Badges Row: Streak & XP (glassmorphism)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          // Streak Badge
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(15.dp))
              .background(Color.White.copy(alpha = 0.10f))
              .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(15.dp))
              .padding(vertical = 9.dp, horizontal = 12.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = PremiumGold400,
                modifier = Modifier.size(17.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "$streakCount Day Streak",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
              )
            }
          }

          // XP Badge
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(15.dp))
              .background(Color.White.copy(alpha = 0.10f))
              .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(15.dp))
              .padding(vertical = 9.dp, horizontal = 12.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "XP",
                tint = PremiumGold400,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "$xpCount XP",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Learner level progress (premium touch)
        val levelProgress = ((xpCount % 300) / 300f).coerceIn(0.05f, 1f)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Text(
            text = "Level ${xpCount / 300 + 1}",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = PremiumGold400,
          )
          LinearProgressIndicator(
            progress = levelProgress,
            modifier = Modifier
              .weight(1f)
              .height(6.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = PremiumGold400,
            trackColor = Color.White.copy(alpha = 0.18f),
          )
          Text(
            text = "${xpCount % 300}/300",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFCBD8F2),
          )
        }
      }
    }
  }
}

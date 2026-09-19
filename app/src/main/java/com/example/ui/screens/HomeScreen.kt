package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeroHeader
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.CountBadge
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.IndigoGradient
import com.example.ui.theme.PremiumEmptyState
import com.example.ui.theme.PremiumGradientButton
import com.example.ui.theme.PremiumSuccess600
import com.example.ui.theme.SectionHeader
import com.example.ui.theme.SkyGradient
import com.example.ui.theme.SuccessGradient
import com.example.ui.theme.TealGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.VioletGradient
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextTertiary
import com.example.ui.theme.rememberPressInteraction
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
  viewModel: MainViewModel,
  onNavigateToTab: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  val topSuggestions by viewModel.topSuggestions.collectAsState()
  val syncStatus by viewModel.syncStatus.collectAsState()
  val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
  val streak by viewModel.streakCount.collectAsState()
  val xp by viewModel.userXp.collectAsState()
  val savedPdfIds by viewModel.savedPdfIds.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(premiumBackground())
      .testTag("home_screen_lazy_column"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // 1. Hero Header Banner (TOWFIK EXCLUSIVE)
    item {
      HeroHeader(
        streakCount = streak,
        xpCount = xp,
        onNotificationClick = {
          Toast.makeText(context, "All Madhyamik 2026 Alert Notifications are Active! 🔔", Toast.LENGTH_SHORT).show()
        },
      )
    }

    // 2. FCM Push Notification Alert Card (premium glass green)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), alpha = 0.10f)
          .testTag("fcm_alert_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (dark) Color(0xFF123524) else Color(0xFFF0FDF4),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (dark) Color(0xFF1F5C38) else Color(0xFFBBF7D0),
        ),
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            GradientIconTile(
              icon = Icons.Default.NotificationsActive,
              gradient = SuccessGradient,
              contentDescription = "Alerts",
              size = 46.dp,
              cornerRadius = 14.dp,
              iconSize = 23.dp,
            )
            Column(modifier = Modifier.weight(1f, fill = false)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Text(
                  text = "Push Notifications Active",
                  fontSize = 13.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (dark) Color(0xFFBBF7D0) else Color(0xFF15803D),
                )
                Box(
                  modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF22C55E)),
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Real-time alerts when Admin publishes new materials",
                fontSize = 11.5.sp,
                color = if (dark) Color(0xFF86EFAC) else Color(0xFF166534),
                lineHeight = 15.sp,
              )
            }
          }

          Spacer(modifier = Modifier.size(8.dp))
          Button(
            onClick = {
              Toast.makeText(context, "Latest update: Madhyamik 2026 suggestions updated by Towfik Sir!", Toast.LENGTH_LONG).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = PremiumSuccess600),
            shape = RoundedCornerShape(11.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 9.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
            modifier = Modifier.testTag("open_alerts_btn"),
          ) {
            Text("Open Alerts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // 3. Admin / Student Mode Card (premium gradient border)
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .premiumShadow(elevation = 7.dp, shape = RoundedCornerShape(22.dp), alpha = 0.12f)
          .clip(RoundedCornerShape(22.dp))
          .background(
            if (isAdmin) SuccessGradient else BrandGradient,
          )
          .padding(1.5.dp)
          .testTag("auth_mode_card"),
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.5.dp))
            .background(premiumCard()),
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              GradientIconTile(
                icon = if (isAdmin) Icons.Default.VerifiedUser else Icons.Default.Lock,
                gradient = if (isAdmin) SuccessGradient else BrandGradient,
                contentDescription = "Mode",
                size = 46.dp,
                cornerRadius = 14.dp,
                iconSize = 23.dp,
              )
              Column {
                Text(
                  text = if (isAdmin) "Admin Console Active" else "Student Mode View",
                  fontSize = 14.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = premiumTextPrimary(),
                )
                Text(
                  text = if (isAdmin) "Logged in as Towfik Sir (Full Control)" else "Tap to log in as Admin",
                  fontSize = 12.sp,
                  color = premiumTextTertiary(),
                )
              }
            }

            Button(
              onClick = { onNavigateToTab(5) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isAdmin) PremiumSuccess600 else TowfikPrimaryBlue,
              ),
              shape = RoundedCornerShape(11.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 9.dp),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
              modifier = Modifier.testTag("admin_login_header_btn"),
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = "Login",
                  tint = Color.White,
                  modifier = Modifier.size(14.dp),
                )
                Text(
                  text = if (isAdmin) "Manage" else "Login",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                )
              }
            }
          }
        }
      }
    }

    // 4. Exclusive Learning Hub 2x2 Grid
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
          title = "Exclusive Learning Hub",
          subtitle = "Everything for Madhyamik 2026",
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          HubButton(
            title = "Smart Search",
            subtitle = "Find Any Note & PYQ",
            icon = Icons.Default.Search,
            iconBg = Color(0xFFE0F2FE),
            iconTint = Color(0xFF0284C7),
            gradient = SkyGradient,
            onClick = { onNavigateToTab(1) },
            modifier = Modifier.weight(1f),
          )
          HubButton(
            title = "Edu Notes",
            subtitle = "Class 9-12 Notes",
            icon = Icons.Default.MenuBook,
            iconBg = Color(0xFFE0E7FF),
            iconTint = Color(0xFF4338CA),
            gradient = IndigoGradient,
            onClick = { onNavigateToTab(2) },
            modifier = Modifier.weight(1f),
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          HubButton(
            title = "PYQ Papers",
            subtitle = "Madhyamik Solved",
            icon = Icons.Default.Quiz,
            iconBg = Color(0xFFEDE9FE),
            iconTint = Color(0xFF7C3AED),
            gradient = VioletGradient,
            onClick = { onNavigateToTab(3) },
            modifier = Modifier.weight(1f),
          )
          HubButton(
            title = "Admin Panel",
            subtitle = "Manage & Upload",
            icon = Icons.Default.AdminPanelSettings,
            iconBg = Color(0xFFCCFBF1),
            iconTint = Color(0xFF0D9488),
            gradient = TealGradient,
            onClick = { onNavigateToTab(5) },
            modifier = Modifier.weight(1f),
          )
        }
      }
    }

    // 5. Firebase Firestore Cloud Sync Card (premium gradient banner)
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .premiumShadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), alpha = 0.20f)
          .clip(RoundedCornerShape(20.dp))
          .background(BrandGradient)
          .testTag("firestore_sync_card"),
      ) {
        // Glass decoration
        Box(
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(140.dp)
            .background(Color.White.copy(alpha = 0.07f), CircleShape),
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(13.dp),
        ) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(15.dp))
              .background(Color.White.copy(alpha = 0.16f))
              .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Default.CloudSync,
              contentDescription = "Sync",
              tint = Color.White,
              modifier = Modifier.size(25.dp),
            )
          }
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Firebase Firestore Cloud Sync",
              fontSize = 14.5.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = syncStatus.ifBlank { "Upload questions & sync class data in real-time" },
              fontSize = 11.5.sp,
              color = Color(0xFFD6E4FF),
              maxLines = 2,
              lineHeight = 15.sp,
            )
          }
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF22C55E))
              .padding(horizontal = 8.dp, vertical = 4.dp),
          ) {
            Text(
              text = "LIVE",
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              color = Color.White,
              letterSpacing = 0.8.sp,
            )
          }
        }
      }
    }

    // 6. Madhyamik 2026 Top Suggestions Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.weight(1f, fill = false),
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(11.dp))
              .background(GoldGradient),
            contentAlignment = Alignment.Center,
          ) {
            Text(text = "✨", fontSize = 16.sp)
          }
          Column {
            Text(
              text = "Madhyamik 2026 Top Suggestions",
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold,
              color = premiumTextPrimary(),
            )
            if (topSuggestions.isNotEmpty()) {
              Text(
                text = "${topSuggestions.size} curated sets by Towfik Sir",
                fontSize = 11.5.sp,
                color = premiumTextTertiary(),
              )
            }
          }
        }

        if (topSuggestions.isNotEmpty()) {
          CountBadge(text = "${topSuggestions.size} sets")
        }
      }
    }

    // 7. Top Suggestion Cards List or Empty State
    if (topSuggestions.isEmpty()) {
      item {
        PremiumEmptyState(
          icon = Icons.Default.CloudSync,
          title = "Cloud Firestore Connected",
          subtitle = "No study materials in Firestore yet. Admin (Towfik Sir) can upload 2026 suggestions and question sets.",
          actionLabel = if (isAdmin) "+ Upload to Firestore" else null,
          onActionClick = if (isAdmin) {
            { viewModel.openAddEditDialog(null) }
          } else {
            null
          },
        )
      }
    } else {
      items(topSuggestions, key = { it.id }) { item ->
        val isSaved = savedPdfIds.contains(item.id)
        QuestionAnswerCard(
          item = item,
          isAdmin = isAdmin,
          isSaved = isSaved,
          onToggleSave = {
            val saved = viewModel.toggleSavedPdf(item.id)
            val msg = if (saved) "Saved '${item.title.take(20)}' to PDF Tab! 📑" else "Removed from saved PDFs"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          },
          onPdfClick = { viewModel.openPdfPreview(item) },
          onEditClick = { viewModel.openAddEditDialog(item) },
          onDeleteClick = { viewModel.deleteStudyItem(item.id) },
        )
      }

      item {
        // View-all CTA under the list (keeps original test tag working)
        Box(modifier = Modifier.testTag("view_all_suggestions_btn")) {
          PremiumGradientButton(
            text = "View All Suggestions in Smart Search",
            icon = Icons.Default.Search,
            onClick = { onNavigateToTab(1) },
            height = 48.dp,
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(26.dp))
    }
  }
}

@Composable
fun HubButton(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconBg: Color,
  iconTint: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  gradient: Brush? = null
) {
  val interaction = rememberPressInteraction()
  Card(
    modifier = modifier
      .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(19.dp), alpha = 0.10f)
      .testTag("hub_btn_${title.lowercase().replace(" ", "_")}"),
    shape = RoundedCornerShape(19.dp),
    colors = CardDefaults.cardColors(containerColor = premiumCard()),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
    onClick = onClick,
    interactionSource = interaction,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      GradientIconTile(
        icon = icon,
        gradient = gradient ?: BrandGradient,
        contentDescription = title,
        size = 46.dp,
        cornerRadius = 14.dp,
        iconSize = 23.dp,
      )
      Column {
        Text(
          text = title,
          fontSize = 14.5.sp,
          fontWeight = FontWeight.ExtraBold,
          color = premiumTextPrimary(),
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = subtitle,
          fontSize = 11.5.sp,
          color = premiumTextTertiary(),
          fontWeight = FontWeight.Medium,
        )
      }
      // Premium mini progress accent
      Box(
        modifier = Modifier
          .height(4.dp)
          .fillMaxWidth(0.55f)
          .clip(RoundedCornerShape(2.dp))
          .background(gradient ?: BrandGradient),
      )
    }
  }
}

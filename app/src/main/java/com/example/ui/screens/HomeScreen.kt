package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeroHeader
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.TowfikAccentGold
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val topSuggestions by viewModel.topSuggestions.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val xp by viewModel.userXp.collectAsState()
    val savedPdfIds by viewModel.savedPdfIds.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Header Banner (TOWFIK EXCLUSIVE)
        item {
            HeroHeader(
                streakCount = streak,
                xpCount = xp,
                onNotificationClick = {
                    Toast.makeText(context, "All Madhyamik 2026 Alert Notifications are Active! 🔔", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 2. FCM Push Notification Alert Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, Color(0xFFDCFCE7), RoundedCornerShape(18.dp))
                    .testTag("fcm_alert_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Alerts",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "FCM Push Notifications Active",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                            Text(
                                text = "Real-time alerts enabled when Admin publishes new materials",
                                fontSize = 11.5.sp,
                                color = Color(0xFF166534),
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Latest update: Madhyamik 2026 suggestions updated by Towfik Sir!", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("open_alerts_btn")
                    ) {
                        Text("Open Alerts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Admin / Student Mode Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, CardBorderColor, RoundedCornerShape(18.dp))
                    .testTag("auth_mode_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isAdmin) Color(0xFFDCFCE7) else Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAdmin) Icons.Default.VerifiedUser else Icons.Default.Lock,
                                contentDescription = "Mode",
                                tint = if (isAdmin) Color(0xFF16A34A) else TowfikPrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isAdmin) "Admin Console Active" else "Student Mode View",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = if (isAdmin) "Logged in as Towfik Sir (Full Control)" else "Tap to log in as Admin",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToTab(5) }, // Admin tab
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAdmin) Color(0xFF16A34A) else TowfikPrimaryBlue
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("admin_login_header_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Login",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isAdmin) "Manage Admin" else "Admin Login",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 4. Exclusive Learning Hub 2x2 Grid
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Exclusive Learning Hub",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubButton(
                        title = "Smart Search",
                        subtitle = "Find Any Note & PYQ",
                        icon = Icons.Default.Search,
                        iconBg = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF0284C7),
                        onClick = { onNavigateToTab(1) },
                        modifier = Modifier.weight(1f)
                    )
                    HubButton(
                        title = "Edu Notes",
                        subtitle = "Class 9-12 Notes",
                        icon = Icons.Default.MenuBook,
                        iconBg = Color(0xFFE0E7FF),
                        iconTint = Color(0xFF4338CA),
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubButton(
                        title = "PYQ Papers",
                        subtitle = "Madhyamik Solved",
                        icon = Icons.Default.Quiz,
                        iconBg = Color(0xFFEDE9FE),
                        iconTint = Color(0xFF7C3AED),
                        onClick = { onNavigateToTab(3) },
                        modifier = Modifier.weight(1f)
                    )
                    HubButton(
                        title = "Admin Panel",
                        subtitle = "Manage & Upload",
                        icon = Icons.Default.AdminPanelSettings,
                        iconBg = Color(0xFFCCFBF1),
                        iconTint = Color(0xFF0D9488),
                        onClick = { onNavigateToTab(5) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. Firebase Firestore Cloud Sync Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(18.dp))
                    .testTag("firestore_sync_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TowfikPrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Sync",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Firebase Firestore Cloud Sync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                        Text(
                            text = "Upload questions & sync class data in real-time",
                            fontSize = 12.sp,
                            color = Color(0xFF0284C7)
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "✨", fontSize = 16.sp)
                    Text(
                        text = "Madhyamik 2026 Top Suggestions",
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                TextButton(
                    onClick = { onNavigateToTab(1) },
                    modifier = Modifier.testTag("view_all_suggestions_btn")
                ) {
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TowfikPrimaryBlue
                    )
                }
            }
        }

        // 7. Top Suggestion Cards List or Empty State
        if (topSuggestions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Cloud",
                            tint = TowfikPrimaryBlue,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "Cloud Firestore Connected",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "No study materials in Firestore yet. Admin (Towfik Sir) can upload 2026 suggestions and question sets.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        if (isAdmin) {
                            Button(
                                onClick = { viewModel.openAddEditDialog(null) },
                                colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Text("+ Upload to Firestore", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
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
                    onDeleteClick = { viewModel.deleteStudyItem(item.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("hub_btn_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

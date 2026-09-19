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
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.material3.TextButton
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
import com.example.ui.components.PremiumEmptyState
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.TowfikLightBg
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
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val xp by viewModel.userXp.collectAsState()
    val savedPdfIds by viewModel.savedPdfIds.collectAsState()
    val items by viewModel.items.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TowfikLightBg)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroHeader(
                streakCount = streak,
                xpCount = xp,
                onNotificationClick = {
                    Toast.makeText(context, "All Madhyamik 2026 Alert Notifications are Active! 🔔", Toast.LENGTH_SHORT).show()
                }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStatCard("Notes", "${items.size}", Color(0xFFEFF6FF), TowfikPrimaryBlue, Modifier.weight(1f))
                MiniStatCard("Suggestions", "${topSuggestions.size}", Color(0xFFFEF3C7), Color(0xFFB45309), Modifier.weight(1f))
                MiniStatCard("XP", "$xp", Color(0xFFF0FDF4), Color(0xFF15803D), Modifier.weight(1f))
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(20.dp))
                    .testTag("fcm_alert_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF22C55E), Color(0xFF15803D)))
                                ),
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
                                text = "Live exam alerts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                            Text(
                                text = "Push notices when Towfik Sir publishes new papers",
                                fontSize = 12.sp,
                                color = Color(0xFF166534),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Latest update: Madhyamik 2026 suggestions updated by Towfik Sir!", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("open_alerts_btn")
                    ) {
                        Text("Alerts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, CardBorderColor, RoundedCornerShape(20.dp))
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
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
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
                                text = if (isAdmin) "Admin console active" else "Student workspace",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = if (isAdmin) "Towfik Sir • full publish control" else "Browse notes, PYQs & PDFs",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToTab(5) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAdmin) Color(0xFF16A34A) else TowfikPrimaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp),
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
                                text = if (isAdmin) "Manage" else "Admin",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionHeader(title = "Learning hub")
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubButton(
                        title = "Smart Search",
                        subtitle = "Find any note & PYQ",
                        icon = Icons.Default.Search,
                        iconBg = Color(0xFFE0F2FE),
                        iconTint = Color(0xFF0284C7),
                        onClick = { onNavigateToTab(1) },
                        modifier = Modifier.weight(1f)
                    )
                    HubButton(
                        title = "Edu Notes",
                        subtitle = "Class 9–12 chapters",
                        icon = Icons.Default.MenuBook,
                        iconBg = Color(0xFFE0E7FF),
                        iconTint = Color(0xFF4338CA),
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HubButton(
                        title = "PYQ Papers",
                        subtitle = "Solved board papers",
                        icon = Icons.Default.Quiz,
                        iconBg = Color(0xFFEDE9FE),
                        iconTint = Color(0xFF7C3AED),
                        onClick = { onNavigateToTab(3) },
                        modifier = Modifier.weight(1f)
                    )
                    HubButton(
                        title = "Admin Panel",
                        subtitle = "Publish & manage",
                        icon = Icons.Default.AdminPanelSettings,
                        iconBg = Color(0xFFCCFBF1),
                        iconTint = Color(0xFF0D9488),
                        onClick = { onNavigateToTab(5) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFBAE6FD), RoundedCornerShape(20.dp))
                    .testTag("firestore_sync_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                            .size(44.dp)
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
                            text = "Cloud library sync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C4A6E)
                        )
                        Text(
                            text = syncStatus,
                            fontSize = 12.sp,
                            color = Color(0xFF0369A1),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFC9A227))
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "2026 suggestions",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
                TextButton(
                    onClick = { onNavigateToTab(1) },
                    modifier = Modifier.testTag("view_all_suggestions_btn")
                ) {
                    Text(
                        text = "View all",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TowfikPrimaryBlue
                    )
                }
            }
        }

        if (topSuggestions.isEmpty()) {
            item {
                PremiumEmptyState(
                    icon = Icons.Default.CloudSync,
                    title = "No suggestions published yet",
                    message = "When Towfik Sir uploads 2026 suggestion sets they will appear here instantly.",
                    action = if (isAdmin) {
                        {
                            Button(
                                onClick = { viewModel.openAddEditDialog(null) },
                                colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("+ Upload to library", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    } else null
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
                    onDeleteClick = { viewModel.deleteStudyItem(item.id) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(28.dp)) }
    }
}

@Composable
private fun MiniStatCard(
    label: String,
    value: String,
    bg: Color,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg.copy(alpha = 0.35f))
                .padding(12.dp)
        ) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = tint)
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
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
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, CardBorderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("hub_btn_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
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
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

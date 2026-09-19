package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SubjectItem
import com.example.ui.components.AdminAuthCard
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.HeaderGradient
import com.example.ui.theme.DecorativeHeaderBackground
import com.example.ui.theme.PremiumGradientButton
import com.example.ui.theme.PremiumSuccess600
import com.example.ui.theme.SectionHeader
import com.example.ui.theme.SuccessGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextTertiary
import com.example.viewmodel.MainViewModel

@Composable
fun AdminScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue
  val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
  val syncStatus by viewModel.syncStatus.collectAsState()
  val items by viewModel.items.collectAsState()
  val subjects by viewModel.subjects.collectAsState()

  var showAddEditSubjectDialog by remember { mutableStateOf(false) }
  var editingSubject by remember { mutableStateOf<SubjectItem?>(null) }
  var subjectEnglishName by remember { mutableStateOf("") }
  var subjectBengaliName by remember { mutableStateOf("") }

  if (showAddEditSubjectDialog) {
    AlertDialog(
      onDismissRequest = {
        showAddEditSubjectDialog = false
        editingSubject = null
      },
      title = {
        Text(
          text = if (editingSubject != null) "Edit Subject" else "Add New Subject (বিষয় যোগ করুন)",
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = subjectEnglishName,
            onValueChange = { subjectEnglishName = it },
            label = { Text("Subject Name (English)") },
            placeholder = { Text("e.g. Life Science") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accent),
          )

          OutlinedTextField(
            value = subjectBengaliName,
            onValueChange = { subjectBengaliName = it },
            label = { Text("Subject Name (বাংলা)") },
            placeholder = { Text("যেমনঃ জীবন বিজ্ঞান") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = accent),
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (subjectEnglishName.isNotBlank()) {
              val subjId = editingSubject?.id ?: "subj_${System.currentTimeMillis()}"
              val newSubj = SubjectItem(
                id = subjId,
                displayName = subjectEnglishName.trim(),
                bengaliName = subjectBengaliName.trim().ifBlank { subjectEnglishName.trim() },
                iconName = "menu_book",
                isCustom = true,
              )
              viewModel.saveSubject(newSubj)
              showAddEditSubjectDialog = false
              editingSubject = null
              Toast.makeText(context, "Subject saved and synced to Firestore!", Toast.LENGTH_SHORT).show()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
          shape = RoundedCornerShape(10.dp),
        ) {
          Text("Save to Firestore", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showAddEditSubjectDialog = false
          editingSubject = null
        }) {
          Text("Cancel")
        }
      },
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(premiumBackground())
      .testTag("admin_screen_lazy_column"),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    // Premium gradient header
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
      ) {
        DecorativeHeaderBackground(gradient = HeaderGradient, modifier = Modifier.matchParentSize())
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(RoundedCornerShape(17.dp))
              .background(Color.White.copy(alpha = 0.16f))
              .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(17.dp)),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = if (isAdmin) Icons.Default.VerifiedUser else Icons.Default.Security,
              contentDescription = "Admin Shield",
              tint = Color.White,
              modifier = Modifier.size(29.dp),
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Towfik Admin Console",
              fontSize = 21.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(if (isAdmin) Color(0xFF4ADE80) else Color(0xFFFBBF24)),
              )
              Text(
                text = if (isAdmin) "Admin Mode • Full Edit & Firestore Sync" else "Student Mode • Admin Login Required",
                fontSize = 12.sp,
                color = Color(0xFFD6E4FF),
                fontWeight = FontWeight.Medium,
              )
            }
          }
        }
      }
    }

    // Web App & Online Hub Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), alpha = 0.10f),
        colors = CardDefaults.cardColors(
          containerColor = if (dark) Color(0xFF16294D) else Color(0xFFEFF6FF),
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (dark) Color(0xFF2A4A7F) else Color(0xFFBFDBFE),
        ),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
          verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            GradientIconTile(
              icon = Icons.Default.Language,
              gradient = BrandGradient,
              contentDescription = "Web App",
              size = 40.dp,
              cornerRadius = 12.dp,
              iconSize = 21.dp,
            )
            Text(
              text = "Towfik Exclusive Web App & Cloud Hub",
              fontSize = 14.5.sp,
              fontWeight = FontWeight.ExtraBold,
              color = if (dark) Color(0xFFDBEAFE) else Color(0xFF1E3A8A),
            )
          }

          Text(
            text = "Access all study materials, Madhyamik suggestions, and PDF downloads directly in any browser or web device.",
            fontSize = 12.5.sp,
            color = if (dark) Color(0xFFB6C2D6) else Color(0xFF334155),
            lineHeight = 18.sp,
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.webAppUrl))
                context.startActivity(intent)
              },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
              shape = RoundedCornerShape(12.dp),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
            ) {
              Icon(
                imageVector = Icons.Default.OpenInBrowser,
                contentDescription = "Open Web App",
                tint = Color.White,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text("Open Web App", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_SUBJECT, "Towfik Exclusive Web App Link")
                  putExtra(
                    Intent.EXTRA_TEXT,
                    "🌐 *Towfik Exclusive Education Web App*\n" +
                      "Access Madhyamik 2026 Suggestions, Notes & Solved PYQs:\n" +
                      viewModel.webAppUrl,
                  )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Web App Link"))
              },
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.5.dp, accent),
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Link",
                tint = accent,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text("Share", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = accent)
            }
          }
        }
      }
    }

    if (!isAdmin) {
      item {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          AdminAuthCard(
            onLoginSuccess = {
              Toast.makeText(context, "Welcome Towfik Sir! Admin Access Granted.", Toast.LENGTH_SHORT).show()
            },
            onLoginAttempt = { email, pass ->
              viewModel.loginAdmin(email, pass)
            },
          )
        }
      }
    } else {
      // Admin Dashboard Controls
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          // Stats row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            AdminStatCard(
              value = "${items.size}",
              label = "Materials",
              modifier = Modifier.weight(1f),
            )
            AdminStatCard(
              value = "${subjects.size}",
              label = "Subjects",
              modifier = Modifier.weight(1f),
            )
            AdminStatCard(
              value = "${items.sumOf { if (it.qaList.isNotEmpty()) it.qaList.size else 1 }}",
              label = "Questions",
              modifier = Modifier.weight(1f),
            )
          }

          // Real-time Cloud Firestore Status
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
              containerColor = if (dark) Color(0xFF123524) else Color(0xFFF0FDF4),
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (dark) Color(0xFF1F5C38) else Color(0xFFBBF7D0),
            ),
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(11.dp),
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(RoundedCornerShape(13.dp))
                  .background(SuccessGradient),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  imageVector = Icons.Default.CloudDone,
                  contentDescription = "Sync",
                  tint = Color.White,
                  modifier = Modifier.size(23.dp),
                )
              }
              Column {
                Text(
                  text = "Live Firestore Cloud Database",
                  fontSize = 13.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (dark) Color(0xFFBBF7D0) else Color(0xFF15803D),
                )
                Text(
                  text = syncStatus,
                  fontSize = 11.5.sp,
                  color = if (dark) Color(0xFF86EFAC) else Color(0xFF166534),
                )
              }
            }
          }

          // Upload Action Button
          Box(modifier = Modifier.testTag("admin_upload_new_btn")) {
            PremiumGradientButton(
              text = "Upload Question / Suggestion to Firestore",
              icon = Icons.Default.Add,
              onClick = { viewModel.openAddEditDialog(null) },
            )
          }

          // Logout Admin Button
          OutlinedButton(
            onClick = {
              viewModel.logoutAdmin()
              Toast.makeText(context, "Logged out of Admin Console", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("admin_logout_btn"),
            shape = RoundedCornerShape(13.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFDC2626).copy(alpha = 0.55f)),
          ) {
            Icon(
              imageVector = Icons.Default.ExitToApp,
              contentDescription = "Logout",
              tint = Color(0xFFDC2626),
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Switch Back to Student Mode",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFDC2626),
            )
          }
        }
      }

      // SUBJECT MANAGEMENT SECTION
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), alpha = 0.10f),
          colors = CardDefaults.cardColors(containerColor = premiumCard()),
          shape = RoundedCornerShape(20.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                GradientIconTile(
                  icon = Icons.Default.Book,
                  gradient = BrandGradient,
                  contentDescription = "Subjects",
                  size = 38.dp,
                  cornerRadius = 12.dp,
                  iconSize = 20.dp,
                )
                Column {
                  Text(
                    text = "Manage Subjects",
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = premiumTextPrimary(),
                  )
                  Text(
                    text = "${subjects.size} subjects live",
                    fontSize = 11.5.sp,
                    color = premiumTextTertiary(),
                  )
                }
              }

              Button(
                onClick = {
                  editingSubject = null
                  subjectEnglishName = ""
                  subjectBengaliName = ""
                  showAddEditSubjectDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = PremiumSuccess600),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(38.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Add Subject",
                  tint = Color.White,
                  modifier = Modifier.size(15.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("+ Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            HorizontalDivider(color = if (dark) Color(0xFF1C2947) else Color(0xFFF1F5F9))

            subjects.forEach { subj ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(13.dp))
                  .background(if (dark) Color(0xFF0E1730) else Color(0xFFF8FAFC))
                  .border(1.dp, premiumBorder(), RoundedCornerShape(13.dp))
                  .padding(horizontal = 12.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Column {
                  Text(
                    text = subj.displayName,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = premiumTextPrimary(),
                  )
                  Text(
                    text = subj.bengaliName,
                    fontSize = 11.5.sp,
                    color = premiumTextTertiary(),
                  )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  IconButton(
                    onClick = {
                      editingSubject = subj
                      subjectEnglishName = subj.displayName
                      subjectBengaliName = subj.bengaliName
                      showAddEditSubjectDialog = true
                    },
                    modifier = Modifier
                      .size(32.dp)
                      .clip(RoundedCornerShape(9.dp))
                      .background(if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF)),
                  ) {
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = "Edit Subject",
                      tint = accent,
                      modifier = Modifier.size(16.dp),
                    )
                  }

                  IconButton(
                    onClick = {
                      viewModel.deleteSubject(subj.id)
                      Toast.makeText(context, "Subject removed from Firestore", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                      .size(32.dp)
                      .clip(RoundedCornerShape(9.dp))
                      .background(if (dark) Color(0xFF3B1420) else Color(0xFFFFF1F2)),
                  ) {
                    Icon(
                      imageVector = Icons.Default.Delete,
                      contentDescription = "Delete Subject",
                      tint = Color(0xFFDC2626),
                      modifier = Modifier.size(16.dp),
                    )
                  }
                }
              }
            }
          }
        }
      }

      item {
        SectionHeader(
          title = "Published Materials",
          subtitle = "${items.size} live in Firestore",
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
        )
      }

      items(items, key = { it.id }) { item ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          QuestionAnswerCard(
            item = item,
            isAdmin = true,
            onPdfClick = { viewModel.openPdfPreview(item) },
            onEditClick = { viewModel.openAddEditDialog(item) },
            onDeleteClick = { viewModel.deleteStudyItem(item.id) },
          )
        }
      }
    }
  }
}

@Composable
private fun AdminStatCard(
  value: String,
  label: String,
  modifier: Modifier = Modifier
) {
  val dark = isSystemInDarkTheme()
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(if (dark) Color(0xFF16294D) else Color(0xFFEFF6FF))
      .border(
        1.dp,
        if (dark) Color(0xFF2A4A7F) else Color(0xFFBFDBFE),
        RoundedCornerShape(16.dp),
      )
      .padding(vertical = 12.dp, horizontal = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(2.dp),
  ) {
    Text(
      text = value,
      fontSize = 19.sp,
      fontWeight = FontWeight.Black,
      color = if (dark) Color(0xFFBFDBFE) else TowfikPrimaryBlue,
    )
    Text(
      text = label,
      fontSize = 10.5.sp,
      fontWeight = FontWeight.SemiBold,
      color = premiumTextTertiary(),
    )
  }
}

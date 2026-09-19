package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
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
import com.example.ui.components.PremiumScreenHeader
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.TowfikLightBg
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.viewmodel.MainViewModel

@Composable
fun AdminScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                    fontSize = 17.sp
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
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = subjectBengaliName,
                        onValueChange = { subjectBengaliName = it },
                        label = { Text("Subject Name (বাংলা)") },
                        placeholder = { Text("যেমনঃ জীবন বিজ্ঞান") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
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
                                isCustom = true
                            )
                            viewModel.saveSubject(newSubj)
                            showAddEditSubjectDialog = false
                            editingSubject = null
                            Toast.makeText(context, "Subject saved and synced to Firestore!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                    shape = RoundedCornerShape(10.dp)
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
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TowfikLightBg)
            .testTag("admin_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PremiumScreenHeader(
                title = "Admin console",
                subtitle = if (isAdmin) "Towfik Sir • full edit & cloud publish" else "Secure login required for publishing",
                icon = Icons.Default.AdminPanelSettings
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Web App",
                            tint = TowfikPrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Web app & cloud hub",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }

                    Text(
                        text = "Open Madhyamik suggestions, notes and PDF downloads in any browser.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 17.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.webAppUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = "Open Web App",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open Web", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                                                viewModel.webAppUrl
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Web App Link"))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Link",
                                tint = TowfikPrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TowfikPrimaryBlue)
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
                        }
                    )
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Sync",
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Live Firestore database",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                                Text(
                                    text = syncStatus,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.openAddEditDialog(null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("admin_upload_new_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Upload",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upload question / suggestion",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.logoutAdmin()
                            Toast.makeText(context, "Logged out of Admin Console", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("admin_logout_btn"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Switch back to student mode",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = "Subjects",
                                    tint = TowfikPrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Manage subjects (${subjects.size})",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }

                            Button(
                                onClick = {
                                    editingSubject = null
                                    subjectEnglishName = ""
                                    subjectBengaliName = ""
                                    showAddEditSubjectDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Subject",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Subject", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        subjects.forEach { subj ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = subj.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                    Text(
                                        text = subj.bengaliName,
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF64748B)
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
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Subject",
                                            tint = TowfikPrimaryBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteSubject(subj.id)
                                            Toast.makeText(context, "Subject removed from Firestore", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Subject",
                                            tint = Color(0xFFDC2626),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Published materials (${items.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            items(items, key = { it.id }) { item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    QuestionAnswerCard(
                        item = item,
                        isAdmin = true,
                        onPdfClick = { viewModel.openPdfPreview(item) },
                        onEditClick = { viewModel.openAddEditDialog(item) },
                        onDeleteClick = { viewModel.deleteStudyItem(item.id) }
                    )
                }
            }
        }
    }
}

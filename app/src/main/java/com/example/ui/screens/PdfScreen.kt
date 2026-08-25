package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.PillSubjectBg
import com.example.ui.theme.PillSubjectText
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.util.PdfGeneratorUtil
import com.example.viewmodel.MainViewModel

@Composable
fun PdfScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val items by viewModel.items.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    val savedPdfIds by viewModel.savedPdfIds.collectAsState()

    var selectedTab by remember { mutableStateOf("All") } // "All" or "Saved"

    val displayedItems = remember(items, savedPdfIds, selectedTab) {
        if (selectedTab == "Saved") {
            items.filter { savedPdfIds.contains(it.id) }
        } else {
            items
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("pdf_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFECEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDFs",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "PDF Downloads & Saved Materials",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Manage, view, and share your compiled Madhyamik & HS question paper PDFs.",
                        fontSize = 12.5.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Tab selection pills: All PDFs & Saved PDFs
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedTab == "All",
                    onClick = { selectedTab = "All" },
                    label = { Text("All PDFs (${items.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TowfikPrimaryBlue,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedTab == "Saved",
                    onClick = { selectedTab = "Saved" },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (selectedTab == "Saved") Color.White else Color(0xFFE11D48)
                            )
                            Text("Saved PDFs (${savedPdfIds.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE11D48),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White
                    )
                )
            }
        }

        // PDF Cards or Empty State
        if (displayedItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
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
                            imageVector = if (selectedTab == "Saved") Icons.Default.BookmarkBorder else Icons.Default.PictureAsPdf,
                            contentDescription = "PDFs",
                            tint = if (selectedTab == "Saved") Color(0xFFE11D48) else Color(0xFFDC2626),
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = if (selectedTab == "Saved") "No Saved PDFs Yet" else "No PDF Materials in Firestore Yet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (selectedTab == "Saved") {
                                "Tap the bookmark/save icon on any chapter or study material card to save it here for fast access."
                            } else {
                                "Materials uploaded to Cloud Firestore by Towfik Sir will appear here with instant multi-page PDF compilation and sharing."
                            },
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(displayedItems, key = { it.id }) { item ->
                val isSaved = savedPdfIds.contains(item.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, CardBorderColor, RoundedCornerShape(20.dp))
                        .testTag("pdf_item_card_${item.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Top Pill, Save bookmark & Admin delete button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PillSubjectBg)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${item.classLevel} • ${item.subject}",
                                    color = PillSubjectText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Save / Bookmark icon button
                                IconButton(
                                    onClick = {
                                        val saved = viewModel.toggleSavedPdf(item.id)
                                        val msg = if (saved) "Saved '${item.title.take(20)}' to PDF Tab! 📑" else "Removed from saved PDFs"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp).testTag("toggle_save_pdf_${item.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = if (isSaved) "Saved" else "Save",
                                        tint = if (isSaved) Color(0xFFE11D48) else Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                if (isAdmin) {
                                    IconButton(
                                        onClick = { viewModel.deleteStudyItem(item.id) },
                                        modifier = Modifier.size(32.dp).testTag("delete_pdf_${item.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFDC2626),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Document Title
                        Text(
                            text = item.title,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Size & Date details
                        Text(
                            text = "${item.qaList.size.coerceAtLeast(1)} Item(s) • ${item.fileSizeKb} KB • ${item.dateAdded}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Buttons Row: Preview Info & Share PDF
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.openPdfPreview(item) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("preview_info_btn_${item.id}"),
                                shape = RoundedCornerShape(10.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = "Preview",
                                    tint = TowfikPrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Preview Info",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TowfikPrimaryBlue
                                )
                            }

                            Button(
                                onClick = {
                                    PdfGeneratorUtil.sharePdf(context, item)
                                    Toast.makeText(context, "Preparing & sharing official PDF document...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("share_pdf_btn_${item.id}"),
                                colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Share PDF",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

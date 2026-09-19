package com.example.ui.screens

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
import com.example.ui.components.PremiumEmptyState
import com.example.ui.components.PremiumScreenHeader
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.PillSubjectBg
import com.example.ui.theme.PillSubjectText
import com.example.ui.theme.TowfikLightBg
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

    var selectedTab by remember { mutableStateOf("All") }

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
            .background(TowfikLightBg)
            .testTag("pdf_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PremiumScreenHeader(
                title = "PDF studio",
                subtitle = "Preview, save and share compiled A4 papers",
                icon = Icons.Default.PictureAsPdf
            )
        }

        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
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
                            Text("Saved (${savedPdfIds.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
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

        if (displayedItems.isEmpty()) {
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    PremiumEmptyState(
                        icon = if (selectedTab == "Saved") Icons.Default.BookmarkBorder else Icons.Default.PictureAsPdf,
                        title = if (selectedTab == "Saved") "No saved PDFs yet" else "No PDF materials yet",
                        message = if (selectedTab == "Saved") {
                            "Tap the bookmark icon on any chapter or study card to keep it here for fast access."
                        } else {
                            "Materials uploaded by Towfik Sir appear here with instant multi-page PDF compilation."
                        },
                        iconTint = if (selectedTab == "Saved") Color(0xFFE11D48) else Color(0xFFDC2626)
                    )
                }
            }
        } else {
            items(displayedItems, key = { it.id }) { item ->
                val isSaved = savedPdfIds.contains(item.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.dp, CardBorderColor, RoundedCornerShape(22.dp))
                        .testTag("pdf_item_card_${item.id}"),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
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

                        Text(
                            text = item.title,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${item.qaList.size.coerceAtLeast(1)} item(s) • ${item.fileSizeKb} KB • ${item.dateAdded}",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.openPdfPreview(item) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("preview_info_btn_${item.id}"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = "Preview",
                                    tint = TowfikPrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Preview",
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
                                    .height(46.dp)
                                    .testTag("share_pdf_btn_${item.id}"),
                                colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                                shape = RoundedCornerShape(12.dp)
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

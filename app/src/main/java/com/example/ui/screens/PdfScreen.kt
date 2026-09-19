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
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.PremiumEmptyState
import com.example.ui.theme.RoseGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary
import com.example.util.PdfGeneratorUtil
import com.example.viewmodel.MainViewModel

@Composable
fun PdfScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue
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

  val totalKb = remember(items) { items.sumOf { it.fileSizeKb } }
  val totalMbText = if (totalKb >= 1024) {
    String.format("%.1f MB", totalKb / 1024f)
  } else {
    "$totalKb KB"
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(premiumBackground())
      .testTag("pdf_screen_lazy_column"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // Premium Top Header
    item {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        GradientIconTile(
          icon = Icons.Default.PictureAsPdf,
          gradient = RoseGradient,
          contentDescription = "PDFs",
          size = 50.dp,
          cornerRadius = 15.dp,
          iconSize = 26.dp,
        )
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "PDF Library",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = premiumTextPrimary(),
          )
          Text(
            text = "${items.size} documents • $totalMbText • A4 ready",
            fontSize = 12.5.sp,
            color = premiumTextTertiary(),
            fontWeight = FontWeight.Medium,
          )
        }
      }
    }

    // Tab selection pills: All PDFs & Saved PDFs
    item {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        FilterChip(
          selected = selectedTab == "All",
          onClick = { selectedTab = "All" },
          label = { Text("All PDFs (${items.size})", fontWeight = FontWeight.Bold, fontSize = 12.5.sp) },
          leadingIcon = if (selectedTab == "All") {
            {
              Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = Color.White,
              )
            }
          } else {
            null
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TowfikPrimaryBlue,
            selectedLabelColor = Color.White,
            containerColor = premiumCard(),
            labelColor = premiumTextSecondary(),
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedTab == "All",
            borderColor = premiumBorder(),
            selectedBorderColor = TowfikPrimaryBlue,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp,
          ),
        )

        FilterChip(
          selected = selectedTab == "Saved",
          onClick = { selectedTab = "Saved" },
          label = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
              Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (selectedTab == "Saved") Color.White else Color(0xFFE11D48),
              )
              Text("Saved (${savedPdfIds.size})", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
            }
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFE11D48),
            selectedLabelColor = Color.White,
            containerColor = premiumCard(),
            labelColor = premiumTextSecondary(),
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedTab == "Saved",
            borderColor = premiumBorder(),
            selectedBorderColor = Color(0xFFE11D48),
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp,
          ),
        )
      }
    }

    if (displayedItems.isEmpty()) {
      item {
        PremiumEmptyState(
          icon = if (selectedTab == "Saved") Icons.Default.BookmarkBorder else Icons.Default.PictureAsPdf,
          title = if (selectedTab == "Saved") "No Saved PDFs Yet" else "No PDF Materials in Firestore Yet",
          subtitle = if (selectedTab == "Saved") {
            "Tap the bookmark icon on any chapter or study material card to save it here for fast access."
          } else {
            "Materials uploaded to Cloud Firestore by Towfik Sir will appear here with instant multi-page PDF compilation and sharing."
          },
          iconGradient = RoseGradient,
        )
      }
    } else {
      items(displayedItems, key = { it.id }) { item ->
        val isSaved = savedPdfIds.contains(item.id)
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp), alpha = 0.10f)
            .testTag("pdf_item_card_${item.id}"),
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = premiumCard()),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            // Gradient accent
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(if (isSaved) RoseGradient else BrandGradient),
            )
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                // Document emblem + pill
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(9.dp),
                  modifier = Modifier.weight(1f, fill = false),
                ) {
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .clip(RoundedCornerShape(13.dp))
                      .background(RoseGradient),
                    contentAlignment = Alignment.Center,
                  ) {
                    Text(
                      text = "PDF",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Black,
                      color = Color.White,
                      letterSpacing = 0.5.sp,
                    )
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .background(if (dark) Color(0xFF1B2B4D) else Color(0xFFEEF2FF))
                      .border(
                        1.dp,
                        if (dark) Color(0xFF2A4A7F) else Color(0xFFC7D7FE),
                        RoundedCornerShape(8.dp),
                      )
                      .padding(horizontal = 10.dp, vertical = 6.dp),
                  ) {
                    Text(
                      text = "${item.classLevel} • ${item.subject}",
                      color = if (dark) Color(0xFFBFDBFE) else Color(0xFF3730A3),
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.Bold,
                    )
                  }
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                  IconButton(
                    onClick = {
                      val saved = viewModel.toggleSavedPdf(item.id)
                      val msg = if (saved) "Saved '${item.title.take(20)}' to PDF Tab! 📑" else "Removed from saved PDFs"
                      Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                      .size(34.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .background(if (isSaved) Color(0xFFFFE4E6) else Color.Transparent)
                      .testTag("toggle_save_pdf_${item.id}"),
                  ) {
                    Icon(
                      imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                      contentDescription = if (isSaved) "Saved" else "Save",
                      tint = if (isSaved) Color(0xFFE11D48) else premiumTextTertiary(),
                      modifier = Modifier.size(21.dp),
                    )
                  }

                  if (isAdmin) {
                    IconButton(
                      onClick = { viewModel.deleteStudyItem(item.id) },
                      modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (dark) Color(0xFF3B1420) else Color(0xFFFFF1F2))
                        .testTag("delete_pdf_${item.id}"),
                    ) {
                      Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(19.dp),
                      )
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(11.dp))

              Text(
                text = item.title,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = premiumTextPrimary(),
                lineHeight = 22.sp,
              )

              Spacer(modifier = Modifier.height(5.dp))

              Text(
                text = "${item.qaList.size.coerceAtLeast(1)} Item(s) • ${item.fileSizeKb} KB • ${item.dateAdded}",
                fontSize = 12.sp,
                color = premiumTextTertiary(),
                fontWeight = FontWeight.Medium,
              )

              Spacer(modifier = Modifier.height(15.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
              ) {
                OutlinedButton(
                  onClick = { viewModel.openPdfPreview(item) },
                  modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .testTag("preview_info_btn_${item.id}"),
                  shape = RoundedCornerShape(13.dp),
                  border = androidx.compose.foundation.BorderStroke(1.5.dp, accent),
                ) {
                  Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Preview",
                    tint = accent,
                    modifier = Modifier.size(17.dp),
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Preview",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
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
                  shape = RoundedCornerShape(13.dp),
                  elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Share PDF",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

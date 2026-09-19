package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.CountBadge
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.PremiumEmptyState
import com.example.ui.theme.VioletGradient
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary
import com.example.viewmodel.MainViewModel

@Composable
fun PyqScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val pyqItems by viewModel.pyqItems.collectAsState()
  val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

  val years = listOf("All Years", "2025", "2024", "2023", "2022", "2020")
  var selectedYear by remember { mutableStateOf("All Years") }

  val filteredList = pyqItems.filter { item ->
    selectedYear == "All Years" || item.pyqYear == selectedYear
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(premiumBackground())
      .testTag("pyq_screen_lazy_column"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    // Premium Header
    item {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        GradientIconTile(
          icon = Icons.Default.Quiz,
          gradient = VioletGradient,
          contentDescription = "PYQ",
          size = 50.dp,
          cornerRadius = 15.dp,
          iconSize = 26.dp,
        )
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Solved PYQ Papers",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = premiumTextPrimary(),
          )
          Text(
            text = "Madhyamik board papers with marking key",
            fontSize = 12.5.sp,
            color = premiumTextTertiary(),
            fontWeight = FontWeight.Medium,
          )
        }
        CountBadge(text = "${filteredList.size} papers")
      }
    }

    // Stats strip
    if (pyqItems.isNotEmpty()) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          PyqStatCard(
            value = "${pyqItems.size}",
            label = "Total Papers",
            modifier = Modifier.weight(1f),
          )
          PyqStatCard(
            value = "${pyqItems.map { it.pyqYear }.distinct().size}",
            label = "Years Covered",
            modifier = Modifier.weight(1f),
          )
          PyqStatCard(
            value = "${pyqItems.sumOf { if (it.qaList.isNotEmpty()) it.qaList.size else 1 }}",
            label = "Solved Q&As",
            modifier = Modifier.weight(1f),
          )
        }
      }
    }

    // Year Selector Chips
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        years.forEach { yr ->
          val selected = selectedYear == yr
          FilterChip(
            selected = selected,
            onClick = { selectedYear = yr },
            label = {
              Text(
                if (yr == "All Years") yr else "$yr Paper",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
              )
            },
            leadingIcon = if (selected) {
              {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                )
              }
            } else {
              null
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Color(0xFF7C3AED),
              selectedLabelColor = Color.White,
              containerColor = premiumCard(),
              labelColor = premiumTextSecondary(),
            ),
            border = FilterChipDefaults.filterChipBorder(
              enabled = true,
              selected = selected,
              borderColor = premiumBorder(),
              selectedBorderColor = Color(0xFF7C3AED),
              borderWidth = 1.dp,
              selectedBorderWidth = 1.dp,
            ),
          )
        }
      }
    }

    if (filteredList.isEmpty()) {
      item {
        PremiumEmptyState(
          icon = Icons.Default.Quiz,
          title = "No PYQ Papers Found in Firestore",
          subtitle = "Previous year solved question papers published to Firestore will be listed here automatically.",
          iconGradient = VioletGradient,
        )
      }
    } else {
      items(filteredList, key = { it.id }) { item ->
        QuestionAnswerCard(
          item = item,
          isAdmin = isAdmin,
          onPdfClick = { viewModel.openPdfPreview(item) },
          onEditClick = { viewModel.openAddEditDialog(item) },
          onDeleteClick = { viewModel.deleteStudyItem(item.id) },
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
private fun PyqStatCard(
  value: String,
  label: String,
  modifier: Modifier = Modifier
) {
  val dark = isSystemInDarkTheme()
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(
        if (dark) Color(0xFF251647) else Color(0xFFF5F3FF),
      )
      .border(
        1.dp,
        if (dark) Color(0xFF452A7A) else Color(0xFFDDD6FE),
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
      color = if (dark) Color(0xFFDDD6FE) else Color(0xFF6D28D9),
    )
    Text(
      text = label,
      fontSize = 10.5.sp,
      fontWeight = FontWeight.SemiBold,
      color = if (dark) Color(0xFFB7A6E8) else Color(0xFF7C6AAE),
    )
  }
}

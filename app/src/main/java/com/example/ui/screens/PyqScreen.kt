package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PremiumEmptyState
import com.example.ui.components.PremiumScreenHeader
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.TowfikLightBg
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
            .background(TowfikLightBg)
            .testTag("pyq_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PremiumScreenHeader(
                title = "Solved PYQ papers",
                subtitle = "Board questions with official marking guidance",
                icon = Icons.Default.Quiz
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                years.forEach { yr ->
                    FilterChip(
                        selected = selectedYear == yr,
                        onClick = { selectedYear = yr },
                        label = {
                            Text(
                                if (yr == "All Years") yr else "$yr Paper",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6D28D9),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    PremiumEmptyState(
                        icon = Icons.Default.Quiz,
                        title = "No PYQ papers for this year",
                        message = "Previous year solved question papers published to the library will list here automatically.",
                        iconTint = Color(0xFF7C3AED)
                    )
                }
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    QuestionAnswerCard(
                        item = item,
                        isAdmin = isAdmin,
                        onPdfClick = { viewModel.openPdfPreview(item) },
                        onEditClick = { viewModel.openAddEditDialog(item) },
                        onDeleteClick = { viewModel.deleteStudyItem(item.id) }
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.theme.TowfikPrimaryBlue
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
            .background(Color(0xFFF8FAFC))
            .testTag("pyq_screen_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEDE9FE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = "PYQ",
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = "Madhyamik Solved PYQ Papers",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "10+ Years Previous Questions with Official Board Marking Key",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                years.forEach { yr ->
                    FilterChip(
                        selected = selectedYear == yr,
                        onClick = { selectedYear = yr },
                        label = { Text(if (yr == "All Years") yr else "$yr Paper", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF7C3AED),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White
                        )
                    )
                }
            }
        }

        // List of PYQ Items or Empty State
        if (filteredList.isEmpty()) {
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
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "PYQ",
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No PYQ Papers Found in Firestore",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Previous year solved question papers published to Firestore will be listed here automatically.",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                QuestionAnswerCard(
                    item = item,
                    isAdmin = isAdmin,
                    onPdfClick = { viewModel.openPdfPreview(item) },
                    onEditClick = { viewModel.openAddEditDialog(item) },
                    onDeleteClick = { viewModel.deleteStudyItem(item.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

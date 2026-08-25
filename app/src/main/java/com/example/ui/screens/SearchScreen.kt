package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.QuestionAnswerCard
import com.example.ui.theme.TowfikAccentGold
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.viewmodel.MainViewModel

@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedClass by viewModel.selectedClass.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val filteredItems by viewModel.filteredItems.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val focusManager = LocalFocusManager.current

    val customSubjects by viewModel.subjects.collectAsState()
    val allSubjectsList: List<Pair<String, String>> = remember(customSubjects) {
        val predefined = listOf(
            "Physical Science" to "ভৌত বিজ্ঞান",
            "Life Science" to "জীবন বিজ্ঞান",
            "Mathematics" to "গণিত",
            "History" to "ইতিহাস",
            "Geography" to "ভূগোল",
            "Bengali" to "বাংলা",
            "English" to "English"
        )
        val predefinedKeys = predefined.map { it.first.lowercase() }.toSet()
        val customMapped = customSubjects
            .filter { it.displayName.lowercase() !in predefinedKeys }
            .map { it.displayName to (it.bengaliName.ifBlank { it.displayName }) }

        listOf("All Subjects" to "সকল বিষয়") + predefined + customMapped
    }

    val marksOptions = listOf(
        "All Materials" to "All",
        "1 Mark" to "1 Mark (MCQ/VSA)",
        "2 Marks" to "2 Marks (SA)",
        "3 Marks" to "3 Marks",
        "4 Marks" to "4 Marks",
        "5 Marks" to "5 Marks (LA)",
        "8 Marks" to "8 Marks (Essay)",
        "Notes & Chapters" to "Chapter Notes",
        "PYQs & Suggestions" to "Suggestions & PYQs"
    )

    val classLevels = listOf("All Classes", "Madhyamik 10", "Class 9")

    val trendingKeywords = listOf(
        "স্নেলের সূত্র", "অক্সিন হরমোন", "দ্বিঘাত সমীকরণ",
        "মহাবিদ্রোহ ১৮৫৭", "বায়ুমণ্ডল", "জ্ঞানচক্ষু",
        "Suggestion 2026", "Optics", "Class 10"
    )

    val hasActiveFilter = searchQuery.isNotBlank() ||
            selectedSubject != "All Subjects" ||
            selectedType != "All Materials" ||
            selectedClass != "All Classes"

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .testTag("search_screen_lazy_column"),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Premium Search Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                        .background(TowfikPrimaryBlue)
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x33FFFFFF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Madhyamik Smart Search",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Find Notes, Q&A by Marks & 2026 Suggestions",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFFE0E7FF)
                                    )
                                }
                            }

                            if (hasActiveFilter) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x33FFFFFF))
                                        .clickable {
                                            viewModel.searchQuery.value = ""
                                            viewModel.selectedSubject.value = "All Subjects"
                                            viewModel.selectedType.value = "All Materials"
                                            viewModel.selectedClass.value = "All Classes"
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestartAlt,
                                            contentDescription = "Reset",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "Reset",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // High-contrast Search Input Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.searchQuery.value = it },
                            placeholder = {
                                Text(
                                    text = "Search questions, formulas, chapters...",
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TowfikPrimaryBlue
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = Color(0xFF64748B)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .testTag("search_input_field"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                        )
                    }
                }
            }

            // 2. Marks-Wise Filter Strip (1, 2, 3, 4, 5, 8 Marks)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Marks & Category Filter:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "1, 2, 3, 4, 5, 8 Marks",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        marksOptions.forEach { (typeKey, label) ->
                            val isSelected = selectedType == typeKey
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.selectedType.value = if (isSelected && typeKey != "All Materials") "All Materials" else typeKey
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = if (typeKey.contains("Mark")) Color(0xFFB45309) else TowfikPrimaryBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 3. Subject Filter Strip (With Bengali Names)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Subject / বিষয়:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allSubjectsList.forEach { (eng, ben) ->
                            val isSelected = selectedSubject == eng
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.selectedSubject.value = if (isSelected && eng != "All Subjects") "All Subjects" else eng
                                },
                                label = {
                                    Text(
                                        text = if (eng == "All Subjects") "All Subjects" else "$ben ($eng)",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TowfikPrimaryBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // 4. Quick Trending Keywords (Tap to search)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Quick Search Tags:",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        trendingKeywords.forEach { kw ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(16.dp))
                                    .clickable { viewModel.searchQuery.value = kw }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = kw,
                                    fontSize = 11.5.sp,
                                    color = TowfikPrimaryBlue,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 5. Results Header & Count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Search Results",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${filteredItems.size} items",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }

                    if (hasActiveFilter) {
                        Text(
                            text = "Filtered",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TowfikAccentGold
                        )
                    }
                }
            }

            // 6. Filtered Results List
            if (filteredItems.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔎",
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No matching questions or notes found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try searching for a subject like 'Physical Science', or clear marks filter.",
                                fontSize = 12.5.sp,
                                color = Color(0xFF64748B),
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextButton(
                                onClick = {
                                    viewModel.searchQuery.value = ""
                                    viewModel.selectedSubject.value = "All Subjects"
                                    viewModel.selectedType.value = "All Materials"
                                }
                            ) {
                                Text("Clear All Filters", fontWeight = FontWeight.Bold, color = TowfikPrimaryBlue)
                            }
                        }
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
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
        }
    }
}

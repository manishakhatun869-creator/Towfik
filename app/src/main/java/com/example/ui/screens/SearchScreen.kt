package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.CountBadge
import com.example.ui.theme.DecorativeHeaderBackground
import com.example.ui.theme.HeaderGradient
import com.example.ui.theme.PremiumAmber700
import com.example.ui.theme.PremiumEmptyState
import com.example.ui.theme.PremiumGold400
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary
import com.example.viewmodel.MainViewModel

@Composable
fun SearchScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue
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
      "English" to "English",
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
    "PYQs & Suggestions" to "Suggestions & PYQs",
  )

  val trendingKeywords = listOf(
    "স্নেলের সূত্র", "অক্সিন হরমোন", "দ্বিঘাত সমীকরণ",
    "মহাবিদ্রোহ ১৮৫৭", "বায়ুমণ্ডল", "জ্ঞানচক্ষু",
    "Suggestion 2026", "Optics", "Class 10",
  )

  val hasActiveFilter = searchQuery.isNotBlank() ||
    selectedSubject != "All Subjects" ||
    selectedType != "All Materials" ||
    selectedClass != "All Classes"

  fun resetFilters() {
    viewModel.searchQuery.value = ""
    viewModel.selectedSubject.value = "All Subjects"
    viewModel.selectedType.value = "All Materials"
    viewModel.selectedClass.value = "All Classes"
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = premiumBackground(),
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(premiumBackground())
        .testTag("search_screen_lazy_column"),
      contentPadding = PaddingValues(bottom = 80.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      // 1. Premium Search Header Banner
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
        ) {
          DecorativeHeaderBackground(gradient = HeaderGradient, modifier = Modifier.matchParentSize())
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 20.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White.copy(alpha = 0.16f))
                    .border(1.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(15.dp)),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp),
                  )
                }
                Column {
                  Text(
                    text = "Madhyamik Smart Search",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                  )
                  Text(
                    text = "Notes, Q&A by Marks & 2026 Suggestions",
                    fontSize = 12.sp,
                    color = Color(0xFFD6E4FF),
                    fontWeight = FontWeight.Medium,
                  )
                }
              }

              if (hasActiveFilter) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.16f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .clickable { resetFilters() }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                  ) {
                    Icon(
                      imageVector = Icons.Default.RestartAlt,
                      contentDescription = "Reset",
                      tint = Color.White,
                      modifier = Modifier.size(14.dp),
                    )
                    Text(
                      text = "Reset",
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Premium floating search bar
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .premiumShadow(elevation = 10.dp, shape = RoundedCornerShape(18.dp), alpha = 0.25f)
                .clip(RoundedCornerShape(18.dp)),
            ) {
              OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = {
                  Text(
                    text = "Search questions, formulas, chapters...",
                    fontSize = 13.5.sp,
                    color = Color(0xFF64748B),
                  )
                },
                leadingIcon = {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(RoundedCornerShape(11.dp))
                      .background(BrandGradient),
                    contentAlignment = Alignment.Center,
                  ) {
                    Icon(
                      imageVector = Icons.Default.Search,
                      contentDescription = "Search",
                      tint = Color.White,
                      modifier = Modifier.size(18.dp),
                    )
                  }
                },
                trailingIcon = {
                  if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                      Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color(0xFF64748B),
                      )
                    }
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.White)
                  .testTag("search_input_field"),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = Color.White,
                  focusedBorderColor = PremiumGold400,
                  unfocusedBorderColor = Color.Transparent,
                  focusedTextColor = Color(0xFF0F172A),
                  unfocusedTextColor = Color(0xFF0F172A),
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
              )
            }
          }
        }
      }

      // 2. Marks-Wise Filter Strip
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Marks & Category Filter",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.ExtraBold,
              color = premiumTextPrimary(),
            )
            Text(
              text = "1 • 2 • 3 • 4 • 5 • 8 Marks",
              fontSize = 11.5.sp,
              color = premiumTextTertiary(),
              fontWeight = FontWeight.SemiBold,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            marksOptions.forEach { (typeKey, label) ->
              val isSelected = selectedType == typeKey
              val isMarks = typeKey.contains("Mark")
              FilterChip(
                selected = isSelected,
                onClick = {
                  viewModel.selectedType.value = if (isSelected && typeKey != "All Materials") "All Materials" else typeKey
                },
                label = {
                  Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = if (isMarks) PremiumAmber700 else TowfikPrimaryBlue,
                  selectedLabelColor = Color.White,
                  containerColor = premiumCard(),
                  labelColor = premiumTextSecondary(),
                ),
                border = FilterChipDefaults.filterChipBorder(
                  enabled = true,
                  selected = isSelected,
                  borderColor = premiumBorder(),
                  selectedBorderColor = if (isMarks) PremiumAmber700 else TowfikPrimaryBlue,
                  borderWidth = 1.dp,
                  selectedBorderWidth = 1.dp,
                ),
              )
            }
          }
        }
      }

      // 3. Subject Filter Strip
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        ) {
          Text(
            text = "Subject / বিষয়",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = premiumTextPrimary(),
            modifier = Modifier.padding(bottom = 8.dp),
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = TowfikPrimaryBlue,
                  selectedLabelColor = Color.White,
                  containerColor = premiumCard(),
                  labelColor = premiumTextSecondary(),
                ),
                border = FilterChipDefaults.filterChipBorder(
                  enabled = true,
                  selected = isSelected,
                  borderColor = premiumBorder(),
                  selectedBorderColor = TowfikPrimaryBlue,
                  borderWidth = 1.dp,
                  selectedBorderWidth = 1.dp,
                ),
              )
            }
          }
        }
      }

      // 4. Quick Trending Keywords
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        ) {
          Text(
            text = "Quick Search Tags",
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = premiumTextTertiary(),
            modifier = Modifier.padding(bottom = 8.dp),
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
          ) {
            trendingKeywords.forEach { kw ->
              val active = searchQuery == kw
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(18.dp))
                  .background(
                    if (active) BrandGradient
                    else androidx.compose.ui.graphics.Brush.linearGradient(
                      listOf(
                        if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF),
                        if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF),
                      ),
                    ),
                  )
                  .border(
                    1.dp,
                    if (active) Color.Transparent else if (dark) Color(0xFF2A4A7F) else Color(0xFFBFDBFE),
                    RoundedCornerShape(18.dp),
                  )
                  .clickable { viewModel.searchQuery.value = kw }
                  .padding(horizontal = 12.dp, vertical = 7.dp),
              ) {
                Text(
                  text = kw,
                  fontSize = 11.5.sp,
                  color = if (active) Color.White else accent,
                  fontWeight = FontWeight.SemiBold,
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
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Text(
              text = "Search Results",
              fontSize = 16.5.sp,
              fontWeight = FontWeight.ExtraBold,
              color = premiumTextPrimary(),
            )
            CountBadge(text = "${filteredItems.size} items")
          }

          if (hasActiveFilter) {
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (dark) Color(0xFF3A2C10) else Color(0xFFFEF3C7))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            ) {
              Text(
                text = "● Filtered",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (dark) PremiumGold400 else PremiumAmber700,
              )
            }
          }
        }
      }

      // 6. Filtered Results List
      if (filteredItems.isEmpty()) {
        item {
          Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            PremiumEmptyState(
              icon = Icons.Default.SearchOff,
              title = "No matching questions or notes found",
              subtitle = "Try searching for a subject like 'Physical Science', or clear the marks filter.",
              actionLabel = "Clear All Filters",
              onActionClick = { resetFilters() },
            )
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
              onDeleteClick = { viewModel.deleteStudyItem(item.id) },
            )
          }
        }
      }
    }
  }
}

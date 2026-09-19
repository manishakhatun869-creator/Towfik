package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuestionAnswer
import com.example.ui.components.UnifiedQuestionAnswerBox
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.CountBadge
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.IndigoGradient
import com.example.ui.theme.PremiumEmptyState
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
fun NotesScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue
  val items by viewModel.items.collectAsState()
  val customSubjects by viewModel.subjects.collectAsState()
  val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
  val savedPdfIds by viewModel.savedPdfIds.collectAsState()

  val defaultSubjects = listOf("Physical Science", "Life Science", "Mathematics", "History", "Geography", "Bengali", "English")
  val subjects = remember(customSubjects) {
    val list = mutableListOf<String>()
    list.addAll(defaultSubjects)
    customSubjects.forEach { s ->
      val name = s.displayName
      if (name.isNotBlank() && !list.any { it.equals(name, ignoreCase = true) }) {
        list.add(name)
      }
    }
    list
  }

  var currentSubject by remember { mutableStateOf("Physical Science") }

  val subjectItems = items.filter { it.subject.trim().equals(currentSubject.trim(), ignoreCase = true) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(premiumBackground())
      .testTag("notes_screen_lazy_column"),
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
          icon = Icons.Default.MenuBook,
          gradient = IndigoGradient,
          contentDescription = "Notes",
          size = 50.dp,
          cornerRadius = 15.dp,
          iconSize = 26.dp,
        )
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Subject Notes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = premiumTextPrimary(),
          )
          Text(
            text = "Madhyamik 10 • Verified Solutions",
            fontSize = 12.5.sp,
            color = premiumTextTertiary(),
            fontWeight = FontWeight.Medium,
          )
        }
        CountBadge(text = "${subjectItems.size} sets")
      }
    }

    // Subject Filter Scroll
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        subjects.forEach { subj ->
          val isSelected = currentSubject.equals(subj, ignoreCase = true)
          FilterChip(
            selected = isSelected,
            onClick = { currentSubject = subj },
            label = { Text(subj, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            leadingIcon = if (isSelected) {
              {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color.White),
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

    if (subjectItems.isNotEmpty()) {
      subjectItems.forEach { displayItem ->
        val isSaved = savedPdfIds.contains(displayItem.id)

        // Chapter Header Card
        item(key = "header_${displayItem.id}") {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp), alpha = 0.10f)
              .testTag("chapter_header_card_${displayItem.id}"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = premiumCard()),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
          ) {
            Column(modifier = Modifier.fillMaxWidth()) {
              // Gradient accent line
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(4.dp)
                  .background(if (displayItem.isSuggestion2026) GoldGradient else BrandGradient),
              )
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = displayItem.chapterName.ifBlank { displayItem.title },
                      fontSize = 16.5.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = accent,
                    )
                    if (displayItem.title.isNotBlank() && displayItem.title != displayItem.chapterName) {
                      Spacer(modifier = Modifier.height(3.dp))
                      Text(
                        text = displayItem.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = premiumTextPrimary(),
                      )
                    }
                  }

                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                  ) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                          if (displayItem.isSuggestion2026) {
                            Brush.linearGradient(
                              listOf(
                                if (dark) Color(0xFF3A2C10) else Color(0xFFFEF3C7),
                                if (dark) Color(0xFF3A2C10) else Color(0xFFFDE68A),
                              ),
                            )
                          } else {
                            Brush.linearGradient(
                              listOf(
                                if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF),
                                if (dark) Color(0xFF22345A) else Color(0xFFDBEAFE),
                              ),
                            )
                          },
                        )
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    ) {
                      Text(
                        text = if (displayItem.isSuggestion2026) "★ 2026 Suggestion" else "${displayItem.marks} Marks",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (displayItem.isSuggestion2026) {
                          if (dark) Color(0xFFFDE68A) else Color(0xFF92400E)
                        } else {
                          accent
                        },
                      )
                    }

                    IconButton(
                      onClick = {
                        val saved = viewModel.toggleSavedPdf(displayItem.id)
                        val msg = if (saved) "Saved '${displayItem.title.take(20)}' to PDF Tab! 📑" else "Removed from saved PDFs"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                      },
                      modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (isSaved) Color(0xFFFFE4E6) else if (dark) Color(0xFF1A2745) else Color(0xFFF1F5F9))
                        .testTag("save_chapter_pdf_icon_${displayItem.id}"),
                    ) {
                      Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (isSaved) "Saved to PDF Tab" else "Save PDF to PDF Tab",
                        tint = if (isSaved) Color(0xFFE11D48) else premiumTextTertiary(),
                        modifier = Modifier.size(19.dp),
                      )
                    }

                    if (isAdmin) {
                      IconButton(
                        onClick = { viewModel.openAddEditDialog(displayItem) },
                        modifier = Modifier
                          .size(36.dp)
                          .clip(RoundedCornerShape(11.dp))
                          .background(if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF))
                          .testTag("admin_edit_chapter_btn_${displayItem.id}"),
                      ) {
                        Icon(
                          imageVector = Icons.Default.Edit,
                          contentDescription = "Edit Chapter",
                          tint = accent,
                          modifier = Modifier.size(17.dp),
                        )
                      }
                    }
                  }
                }

                if (displayItem.summaryNotes.isNotBlank()) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Box(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(12.dp))
                      .background(if (dark) Color(0xFF0E1730) else Color(0xFFF1F5F9))
                      .border(1.dp, premiumBorder(), RoundedCornerShape(12.dp))
                      .padding(12.dp),
                  ) {
                    Text(
                      text = displayItem.summaryNotes,
                      fontSize = 12.5.sp,
                      color = premiumTextSecondary(),
                      lineHeight = 19.sp,
                    )
                  }
                }
              }
            }
          }
        }

        val allNotesQuestions: List<QuestionAnswer> = if (displayItem.qaList.isNotEmpty()) {
          displayItem.qaList
        } else if (displayItem.question.isNotBlank() || displayItem.answer.isNotBlank()) {
          listOf(QuestionAnswer("Q1", displayItem.marks, displayItem.question, displayItem.answer))
        } else {
          emptyList()
        }

        items(allNotesQuestions, key = { "${displayItem.id}_${it.qNo}" }) { qa ->
          val qIndex = allNotesQuestions.indexOf(qa)
          UnifiedQuestionAnswerBox(
            qa = qa,
            index = qIndex.coerceAtLeast(0),
            isExpanded = true,
            isAdmin = isAdmin,
            onEdit = { viewModel.openAddEditDialog(displayItem) },
            onDelete = {
              viewModel.deleteQuestionFromItem(displayItem.id, qIndex)
              Toast.makeText(context, "Deleted question ${qa.qNo}", Toast.LENGTH_SHORT).show()
            },
            onCopyQuestion = {
              val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
              clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Question", qa.question))
              Toast.makeText(context, "${qa.qNo} question copied!", Toast.LENGTH_SHORT).show()
            },
            onCopyAnswer = {
              val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
              clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Answer", qa.answer))
              Toast.makeText(context, "${qa.qNo} answer copied!", Toast.LENGTH_SHORT).show()
            },
          )
        }

        // Item PDF Action Button Row
        item(key = "pdf_btn_${displayItem.id}") {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Button(
              onClick = { viewModel.openPdfPreview(displayItem) },
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("download_chapter_pdf_btn_${displayItem.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
              shape = RoundedCornerShape(13.dp),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
              Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "PDF",
                tint = Color.White,
                modifier = Modifier.size(19.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "A4 PDF Preview",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
              )
            }

            Button(
              onClick = {
                PdfGeneratorUtil.sharePdf(context, displayItem)
                Toast.makeText(context, "Preparing & sharing official PDF...", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier
                .height(46.dp)
                .testTag("share_chapter_pdf_btn_${displayItem.id}"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
              shape = RoundedCornerShape(13.dp),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.size(17.dp),
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "Share",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
              )
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
        }
      }
    } else {
      item {
        PremiumEmptyState(
          icon = Icons.Default.MenuBook,
          title = "No $currentSubject Notes in Firestore",
          subtitle = "Study materials added for $currentSubject by Towfik Sir will appear exclusively under this subject tab.",
          iconGradient = IndigoGradient,
        )
      }
    }
  }
}

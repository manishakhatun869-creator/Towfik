package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.DarkGreenTint
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.PremiumGold400
import com.example.ui.theme.PremiumRose600
import com.example.ui.theme.SkyGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumAnswerBg
import com.example.ui.theme.premiumAnswerBody
import com.example.ui.theme.premiumAnswerBorder
import com.example.ui.theme.premiumAnswerTitle
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumMutedChipBg
import com.example.ui.theme.premiumQuestionBg
import com.example.ui.theme.premiumQuestionBody
import com.example.ui.theme.premiumQuestionBorder
import com.example.ui.theme.premiumQuestionTitle
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary
import com.example.ui.theme.rememberPressInteraction
import com.example.util.PdfGeneratorUtil
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun QuestionAnswerCard(
  item: StudyItem,
  isAdmin: Boolean = false,
  showFullAnswerDefault: Boolean = true,
  isSaved: Boolean = false,
  onToggleSave: (() -> Unit)? = null,
  onPdfClick: () -> Unit = {},
  onEditClick: () -> Unit = {},
  onDeleteClick: () -> Unit = {},
  onAddQuestionClick: () -> Unit = onEditClick,
  onDeleteQuestion: ((Int) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  var isExpanded by remember { mutableStateOf(showFullAnswerDefault) }
  var showDeleteConfirm by remember { mutableStateOf(false) }

  // Normalize question list: if qaList is not empty use it, otherwise wrap primary question if present
  val allQuestions: List<QuestionAnswer> = remember(item) {
    if (item.qaList.isNotEmpty()) {
      item.qaList
    } else if (item.question.isNotBlank() || item.answer.isNotBlank()) {
      listOf(
        QuestionAnswer(
          qNo = "Q1",
          marks = item.marks,
          question = item.question,
          answer = item.answer,
        ),
      )
    } else {
      emptyList()
    }
  }

  if (showDeleteConfirm) {
    AlertDialog(
      onDismissRequest = { showDeleteConfirm = false },
      title = {
        Text(
          text = "Delete Material & All Questions?",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
        )
      },
      text = {
        Text(
          text = "Are you sure you want to permanently delete '${item.title}' with all its questions from Cloud Firestore?",
          fontSize = 13.5.sp,
          color = Color(0xFF475569),
        )
      },
      confirmButton = {
        Button(
          onClick = {
            showDeleteConfirm = false
            onDeleteClick()
            Toast.makeText(context, "Deleted material from Firestore", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
          shape = RoundedCornerShape(8.dp),
        ) {
          Text("Delete", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = { showDeleteConfirm = false },
          shape = RoundedCornerShape(8.dp),
        ) {
          Text("Cancel")
        }
      },
    )
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .premiumShadow(elevation = 7.dp, shape = RoundedCornerShape(22.dp), alpha = 0.10f)
      .animateContentSize(
        animationSpec = tween(
          durationMillis = 280,
          easing = FastOutSlowInEasing,
        ),
      )
      .testTag("study_item_card_${item.id}"),
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = premiumCard()),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Premium gradient top accent strip
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .background(
            if (item.isSuggestion2026) GoldGradient else BrandGradient,
          ),
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      ) {
        // Header: Category Pill, Suggestion Badge, Save Icon & Admin Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f, fill = false),
          ) {
            // Subject pill — gradient tinted
            val pillBrush: Brush = if (dark) {
              Brush.linearGradient(listOf(Color(0xFF1B2B4D), Color(0xFF22345A)))
            } else {
              Brush.linearGradient(listOf(Color(0xFFEEF2FF), Color(0xFFE0E9FF)))
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9.dp))
                .background(pillBrush)
                .border(
                  1.dp,
                  if (dark) Color(0xFF2A4A7F) else Color(0xFFC7D7FE),
                  RoundedCornerShape(9.dp),
                )
                .padding(horizontal = 9.dp, vertical = 5.dp),
            ) {
              Text(
                text = "${item.classLevel} • ${item.subject}",
                color = if (dark) Color(0xFFBFDBFE) else Color(0xFF3730A3),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }

            if (item.isExclusive) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(9.dp))
                  .background(if (item.isSuggestion2026) GoldGradient else SkyGradient)
                  .padding(horizontal = 8.dp, vertical = 5.dp),
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Exclusive",
                    tint = Color.White,
                    modifier = Modifier.size(11.dp),
                  )
                  Text(
                    text = if (item.isSuggestion2026) "Suggestion 2026" else "PYQ ${item.pyqYear}",
                    color = Color.White,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                  )
                }
              }
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            // Question Count Badge
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9.dp))
                .background(if (dark) DarkGreenTint else Color(0xFFF0FDF4))
                .border(
                  1.dp,
                  if (dark) Color(0xFF1F5C38) else Color(0xFFBBF7D0),
                  RoundedCornerShape(9.dp),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
              Text(
                text = "${allQuestions.size} Q&As",
                color = if (dark) Color(0xFF86EFAC) else Color(0xFF15803D),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
              )
            }

            // Save / Bookmark to PDF Tab Icon
            if (onToggleSave != null) {
              IconButton(
                onClick = onToggleSave,
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSaved) Color(0xFFFFE4E6) else premiumMutedChipBg())
                  .testTag("save_pdf_icon_${item.id}"),
              ) {
                Icon(
                  imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                  contentDescription = if (isSaved) "Saved to PDF tab" else "Save PDF to PDF tab",
                  tint = if (isSaved) Color(0xFFE11D48) else premiumTextTertiary(),
                  modifier = Modifier.size(19.dp),
                )
              }
            }

            if (isAdmin) {
              IconButton(
                onClick = onEditClick,
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF))
                  .testTag("edit_btn_${item.id}"),
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Edit Chapter & Questions",
                  tint = if (dark) BrandSky400 else TowfikPrimaryBlue,
                  modifier = Modifier.size(16.dp),
                )
              }

              IconButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier
                  .size(34.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (dark) Color(0xFF3B1420) else Color(0xFFFFF1F2))
                  .testTag("delete_btn_${item.id}"),
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Material",
                  tint = PremiumRose600,
                  modifier = Modifier.size(16.dp),
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Title
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = premiumTextPrimary(),
          lineHeight = 23.sp,
        )

        if (item.chapterName.isNotBlank()) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
          ) {
            Box(
              modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(GoldGradient),
            )
            Text(
              text = "Chapter: ${item.chapterName}",
              fontSize = 12.5.sp,
              color = premiumTextSecondary(),
              fontWeight = FontWeight.SemiBold,
            )
          }
        }

        // LIST OF ALL QUESTIONS & ANSWERS IN THIS CHAPTER (Unified Single Box Layout)
        if (allQuestions.isNotEmpty()) {
          Spacer(modifier = Modifier.height(6.dp))

          val questionsToDisplay = if (isExpanded) allQuestions else allQuestions.take(1)

          questionsToDisplay.forEachIndexed { index, qa ->
            UnifiedQuestionAnswerBox(
              qa = qa,
              index = index,
              isExpanded = isExpanded,
              isAdmin = isAdmin,
              onEdit = onEditClick,
              onDelete = {
                if (onDeleteQuestion != null) {
                  onDeleteQuestion(index)
                } else {
                  onEditClick()
                }
              },
              onCopyQuestion = {
                copyToClipboard(context, "${qa.qNo}: ${qa.question}")
                Toast.makeText(context, "${qa.qNo.ifBlank { "Question" }} copied! 📋", Toast.LENGTH_SHORT).show()
              },
              onCopyAnswer = {
                copyToClipboard(context, "${qa.qNo} Answer:\n${qa.answer}")
                Toast.makeText(context, "Model answer copied! 📋", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.padding(vertical = 4.dp),
            )
          }

          if (!isExpanded && allQuestions.size > 1) {
            Box(
              modifier = Modifier
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (dark) Color(0xFF1B2B4D) else Color(0xFFEFF6FF))
                .clickable { isExpanded = true }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
              Text(
                text = "+ ${allQuestions.size - 1} more questions — tap Expand to view all",
                fontSize = 11.5.sp,
                color = if (dark) BrandSky400 else TowfikPrimaryBlue,
                fontWeight = FontWeight.Bold,
              )
            }
          }
        }

        // Quick summary notes if any
        if (item.summaryNotes.isNotBlank() && isExpanded) {
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(if (dark) Color(0xFF3A2C10) else Color(0xFFFFFBEB))
              .border(
                1.dp,
                if (dark) Color(0xFF6B531B) else Color(0xFFFDE68A),
                RoundedCornerShape(14.dp),
              )
              .padding(12.dp),
          ) {
            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Summary",
                  tint = PremiumGold400,
                  modifier = Modifier.size(15.dp),
                )
                Text(
                  text = "Key Exam Revision & Formulas",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (dark) Color(0xFFFDE68A) else Color(0xFF92400E),
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = item.summaryNotes,
                fontSize = 12.5.sp,
                color = if (dark) Color(0xFFFDE68A).copy(alpha = 0.85f) else Color(0xFF78350F),
                lineHeight = 19.sp,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Row: PDF Preview, Share PDF & Expand/Collapse Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          // PDF Preview — gradient CTA
          val pdfInteraction = rememberPressInteraction()
          Box(
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .premiumShadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp), alpha = 0.25f)
              .clip(RoundedCornerShape(12.dp))
              .background(BrandGradient)
              .clickable(
                interactionSource = pdfInteraction,
                indication = null,
                onClick = {
                  onPdfClick()
                  Toast.makeText(context, "Opening PDF preview for '${item.title.take(20)}'...", Toast.LENGTH_SHORT).show()
                },
              )
              .testTag("download_pdf_${item.id}"),
            contentAlignment = Alignment.Center,
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "PDF",
                tint = Color.White,
                modifier = Modifier.size(17.dp),
              )
              Text(
                text = "PDF Preview",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
              )
            }
          }

          // Share PDF
          Button(
            onClick = {
              PdfGeneratorUtil.sharePdf(context, item)
              Toast.makeText(context, "Preparing & sharing official PDF...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .height(44.dp)
              .testTag("share_pdf_btn_${item.id}"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share PDF",
              tint = Color.White,
              modifier = Modifier.size(15.dp),
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = "Share",
              fontSize = 12.5.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
            )
          }

          // Expand / Collapse button
          IconButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(premiumMutedChipBg())
              .border(1.dp, premiumBorder(), RoundedCornerShape(12.dp)),
          ) {
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (isExpanded) "Collapse" else "Expand",
              tint = premiumTextSecondary(),
            )
          }
        }
      }
    }
  }
}

/**
 * Single Unified Question and Answer Container Box
 * Top section (Question) in Pink styling.
 * Soft elegant divider.
 * Bottom section (Answer) in Blue styling.
 * Contains soft animation and admin edit/delete controls.
 */
@Composable
fun UnifiedQuestionAnswerBox(
  qa: QuestionAnswer,
  index: Int,
  isExpanded: Boolean = true,
  isAdmin: Boolean = false,
  onEdit: () -> Unit = {},
  onDelete: () -> Unit = {},
  onCopyQuestion: () -> Unit = {},
  onCopyAnswer: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val qNumber = qa.qNo.ifBlank { "Q${index + 1}" }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(15.dp))
      .border(1.dp, premiumBorder(), RoundedCornerShape(15.dp))
      .animateContentSize(
        animationSpec = tween(
          durationMillis = 240,
          easing = FastOutSlowInEasing,
        ),
      )
      .testTag("unified_qa_box_${qa.qNo}"),
    shape = RoundedCornerShape(15.dp),
    colors = CardDefaults.cardColors(containerColor = premiumCard()),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // 1. QUESTION SECTION (Pink Themed Header & Text)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(premiumQuestionBg())
          .padding(12.dp),
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(7.dp),
              modifier = Modifier.weight(1f),
            ) {
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.linearGradient(listOf(Color(0xFFDB2777), Color(0xFFF472B6))),
                  ),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  imageVector = Icons.Default.HelpOutline,
                  contentDescription = "Question",
                  tint = Color.White,
                  modifier = Modifier.size(13.dp),
                )
              }
              Text(
                text = "$qNumber • প্রশ্ন [${qa.marks} Marks]",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = premiumQuestionTitle(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
              IconButton(
                onClick = onCopyQuestion,
                modifier = Modifier.size(28.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = "Copy Question",
                  tint = premiumQuestionTitle(),
                  modifier = Modifier.size(14.dp),
                )
              }

              if (isAdmin) {
                IconButton(
                  onClick = onEdit,
                  modifier = Modifier.size(28.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Question",
                    tint = TowfikPrimaryBlue,
                    modifier = Modifier.size(14.dp),
                  )
                }
                IconButton(
                  onClick = onDelete,
                  modifier = Modifier.size(28.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete Question",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(14.dp),
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = qa.question.ifBlank { "No question text entered" },
            fontSize = 13.5.sp,
            color = premiumQuestionBody(),
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
          )
        }
      }

      // 2. SOFT DIVIDER
      HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = premiumQuestionBorder(),
      )

      // 3. ANSWER SECTION (Blue Themed Header & Text)
      AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(150)),
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(premiumAnswerBg())
            .padding(12.dp),
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier.weight(1f),
              ) {
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(Color(0xFF1D4ED8), Color(0xFF60A5FA)))),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = "Solution",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp),
                  )
                }
                Text(
                  text = "$qNumber • উত্তর ও মডেল সমাধান",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = premiumAnswerTitle(),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }

              IconButton(
                onClick = onCopyAnswer,
                modifier = Modifier.size(28.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = "Copy Answer",
                  tint = premiumAnswerTitle(),
                  modifier = Modifier.size(14.dp),
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = qa.answer.ifBlank { "No model solution entered" },
              fontSize = 13.5.sp,
              color = premiumAnswerBody(),
              lineHeight = 20.sp,
            )
          }
        }
      }
    }
  }
}

// Backward compatibility support functions for callers using QuestionBox/AnswerBox directly
@Composable
fun QuestionBox(
  label: String,
  text: String,
  onCopy: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(13.dp))
      .background(premiumQuestionBg())
      .border(1.dp, premiumQuestionBorder(), RoundedCornerShape(13.dp))
      .padding(12.dp),
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.weight(1f),
        ) {
          Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "Question",
            tint = premiumQuestionTitle(),
            modifier = Modifier.size(15.dp),
          )
          Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = premiumQuestionTitle(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }

        IconButton(
          onClick = onCopy,
          modifier = Modifier.size(24.dp),
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy Question",
            tint = premiumQuestionTitle(),
            modifier = Modifier.size(14.dp),
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = text.ifBlank { "No question text entered" },
        fontSize = 13.5.sp,
        color = premiumQuestionBody(),
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
      )
    }
  }
}

@Composable
fun AnswerBox(
  label: String,
  text: String,
  onCopy: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(13.dp))
      .background(premiumAnswerBg())
      .border(1.dp, premiumAnswerBorder(), RoundedCornerShape(13.dp))
      .padding(12.dp),
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.weight(1f),
        ) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = "Solution",
            tint = premiumAnswerTitle(),
            modifier = Modifier.size(15.dp),
          )
          Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = premiumAnswerTitle(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }

        IconButton(
          onClick = onCopy,
          modifier = Modifier.size(24.dp),
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy Answer",
            tint = premiumAnswerTitle(),
            modifier = Modifier.size(14.dp),
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = text.ifBlank { "No model solution entered" },
        fontSize = 13.5.sp,
        color = premiumAnswerBody(),
        lineHeight = 20.sp,
      )
    }
  }
}

private fun copyToClipboard(context: Context, text: String) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText("Study Material", text)
  clipboard.setPrimaryClip(clip)
}

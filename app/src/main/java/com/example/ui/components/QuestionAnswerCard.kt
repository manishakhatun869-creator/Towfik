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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material.icons.filled.Quiz
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.ui.theme.AnswerBodyColor
import com.example.ui.theme.AnswerBoxBg
import com.example.ui.theme.AnswerBoxBorder
import com.example.ui.theme.AnswerTextColor
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.PillSubjectBg
import com.example.ui.theme.PillSubjectText
import com.example.ui.theme.PillSuggestionBg
import com.example.ui.theme.PillSuggestionText
import com.example.ui.theme.QuestionBodyColor
import com.example.ui.theme.QuestionBoxBg
import com.example.ui.theme.QuestionBoxBorder
import com.example.ui.theme.QuestionTextColor
import com.example.ui.theme.TowfikAccentGold
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.util.PdfGeneratorUtil

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
                    answer = item.answer
                )
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
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete '${item.title}' with all its questions from Cloud Firestore?",
                    fontSize = 13.5.sp,
                    color = Color(0xFF475569)
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
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirm = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorderColor, RoundedCornerShape(20.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 280,
                    easing = FastOutSlowInEasing
                )
            )
            .testTag("study_item_card_${item.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Category Pill, Suggestion Badge, Save Icon & Admin Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PillSubjectBg)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${item.classLevel} • ${item.subject}",
                            color = PillSubjectText,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (item.isExclusive) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PillSuggestionBg)
                                .padding(horizontal = 7.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Exclusive",
                                    tint = TowfikAccentGold,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = if (item.isSuggestion2026) "Suggestion 2026" else "PYQ ${item.pyqYear}",
                                    color = PillSuggestionText,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Question Count Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF0FDF4))
                            .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${allQuestions.size} Q&As",
                            color = Color(0xFF15803D),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Save / Bookmark to PDF Tab Icon
                    if (onToggleSave != null) {
                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("save_pdf_icon_${item.id}")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isSaved) "Saved to PDF tab" else "Save PDF to PDF tab",
                                tint = if (isSaved) Color(0xFFE11D48) else Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    if (isAdmin) {
                        // Edit Action
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("edit_btn_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Chapter & Questions",
                                tint = TowfikPrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Delete Action
                        IconButton(
                            onClick = { showDeleteConfirm = true },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("delete_btn_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Material",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = item.title,
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )

            if (item.chapterName.isNotBlank()) {
                Text(
                    text = "Chapter / Unit: ${item.chapterName}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
                )
            }

            // LIST OF ALL QUESTIONS & ANSWERS IN THIS CHAPTER (Unified Single Box Layout)
            if (allQuestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))

                // Render first question preview always, or all if expanded
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
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (!isExpanded && allQuestions.size > 1) {
                    Text(
                        text = "+ ${allQuestions.size - 1} more questions in this chapter (Tap 'Expand' to view all)",
                        fontSize = 11.5.sp,
                        color = TowfikPrimaryBlue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable { isExpanded = true }
                    )
                }
            }

            // Quick summary notes if any
            if (item.summaryNotes.isNotBlank() && isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Summary",
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Key Exam Revision & Formulas:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.summaryNotes,
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Row: PDF Preview, Share PDF & Expand/Collapse Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // PDF Preview
                Button(
                    onClick = {
                        onPdfClick()
                        Toast.makeText(context, "Opening PDF preview for '${item.title.take(20)}'...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("download_pdf_${item.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PDF Preview",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Share as REAL PDF Format Document
                Button(
                    onClick = {
                        PdfGeneratorUtil.sharePdf(context, item)
                        Toast.makeText(context, "Preparing & sharing official PDF...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("share_pdf_btn_${item.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share PDF",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Share PDF",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Expand / Collapse button
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFF475569)
                    )
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
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 240,
                    easing = FastOutSlowInEasing
                )
            )
            .testTag("unified_qa_box_${qa.qNo}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. QUESTION SECTION (Pink Themed Header & Text)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QuestionBoxBg)
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(QuestionTextColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = "Question",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Text(
                                text = "$qNumber. প্রশ্ন (Question) [${qa.marks} Marks]",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuestionTextColor,
                                maxLines = 1
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            // Copy Question
                            IconButton(
                                onClick = onCopyQuestion,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Question",
                                    tint = QuestionTextColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Admin individual Q&A quick edit/delete
                            if (isAdmin) {
                                IconButton(
                                    onClick = onEdit,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Question",
                                        tint = TowfikPrimaryBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                IconButton(
                                    onClick = onDelete,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete Question",
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = qa.question.ifBlank { "No question text entered" },
                        fontSize = 13.5.sp,
                        color = QuestionBodyColor,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 2. SOFT DIVIDER
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE2E8F0)
            )

            // 3. ANSWER SECTION (Blue Themed Header & Text)
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(150))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AnswerBoxBg)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(AnswerTextColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "Solution",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                                Text(
                                    text = "$qNumber. উত্তর ও পূর্ণাঙ্গ মডেল সমাধান (Model Answer)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AnswerTextColor,
                                    maxLines = 1
                                )
                            }

                            IconButton(
                                onClick = onCopyAnswer,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Answer",
                                    tint = AnswerTextColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = qa.answer.ifBlank { "No model solution entered" },
                            fontSize = 13.5.sp,
                            color = AnswerBodyColor,
                            lineHeight = 20.sp
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
            .clip(RoundedCornerShape(12.dp))
            .background(QuestionBoxBg)
            .border(1.dp, QuestionBoxBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Question",
                        tint = QuestionTextColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuestionTextColor,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Question",
                        tint = QuestionTextColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text.ifBlank { "No question text entered" },
                fontSize = 13.5.sp,
                color = QuestionBodyColor,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
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
            .clip(RoundedCornerShape(12.dp))
            .background(AnswerBoxBg)
            .border(1.dp, AnswerBoxBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Solution",
                        tint = AnswerTextColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AnswerTextColor,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Answer",
                        tint = AnswerTextColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text.ifBlank { "No model solution entered" },
                fontSize = 13.5.sp,
                color = AnswerBodyColor,
                lineHeight = 20.sp
            )
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Study Material", text)
    clipboard.setPrimaryClip(clip)
}

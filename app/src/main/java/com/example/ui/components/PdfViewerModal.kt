package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.ui.theme.AnswerBodyColor
import com.example.ui.theme.AnswerBoxBg
import com.example.ui.theme.AnswerBoxBorder
import com.example.ui.theme.AnswerTextColor
import com.example.ui.theme.QuestionBodyColor
import com.example.ui.theme.QuestionBoxBg
import com.example.ui.theme.QuestionBoxBorder
import com.example.ui.theme.QuestionTextColor
import com.example.ui.theme.TowfikAccentGold
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.util.PdfGeneratorUtil

@Composable
fun PdfViewerModal(
    item: StudyItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isDownloaded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color(0xFFF8FAFC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Header bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "PDF Document Preview",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFE2E8F0))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF334155),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // PDF Sheet Canvas (Printable A4 look)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp)
                    ) {
                        // Printable Header
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "TOWFIK EXCLUSIVE STUDY HUB",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TowfikPrimaryBlue,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "WBBSE MADHYAMIK EXAMINATION 2026",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569)
                            )
                            Text(
                                text = "Class 10 • Subject: ${item.subject} • Chapter: ${item.chapterName.ifBlank { "All Topics" }}",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.5.dp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Title & Metadata
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${item.marks} Marks",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val allPdfQuestions: List<QuestionAnswer> = remember(item) {
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

                        if (allPdfQuestions.isNotEmpty()) {
                            allPdfQuestions.forEachIndexed { index, qa ->
                                val qNum = qa.qNo.ifBlank { "Q${index + 1}" }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Question Box (Pink)
                                    if (qa.question.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(QuestionBoxBg)
                                                .border(1.dp, QuestionBoxBorder, RoundedCornerShape(8.dp))
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "$qNum. প্রশ্ন (Question) [${qa.marks} Marks]:",
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = QuestionTextColor
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = qa.question,
                                                    fontSize = 12.5.sp,
                                                    color = QuestionBodyColor,
                                                    lineHeight = 18.sp
                                                )
                                            }
                                        }
                                    }

                                    // Answer Box (Blue)
                                    if (qa.answer.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(AnswerBoxBg)
                                                .border(1.dp, AnswerBoxBorder, RoundedCornerShape(8.dp))
                                                .padding(10.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "$qNum. উত্তর ও সমাধান (Model Answer):",
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AnswerTextColor
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = qa.answer,
                                                    fontSize = 12.5.sp,
                                                    color = AnswerBodyColor,
                                                    lineHeight = 18.5.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (item.summaryNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Key Exam Formulas & Quick Revisions:",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF334155)
                            )
                            Text(
                                text = item.summaryNotes,
                                fontSize = 11.5.sp,
                                color = Color(0xFF475569),
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Published by Towfik Exclusive Education • Madhyamik 2026 Batch",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val file = PdfGeneratorUtil.generatePdfFile(context, item)
                            if (file != null) {
                                isDownloaded = true
                                PdfGeneratorUtil.sharePdf(context, item)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("pdf_download_confirm_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDownloaded) Color(0xFF16A34A) else TowfikPrimaryBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isDownloaded) "PDF Downloaded ✓" else "Download PDF (${item.fileSizeKb} KB)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            PdfGeneratorUtil.sharePdf(context, item)
                        },
                        modifier = Modifier
                            .weight(0.8f)
                            .height(46.dp)
                            .testTag("pdf_share_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = TowfikPrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share PDF",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TowfikPrimaryBlue
                        )
                    }
                }
            }
        }
    }
}

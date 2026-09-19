package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.PremiumSuccess600
import com.example.ui.theme.RoseGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumAnswerBg
import com.example.ui.theme.premiumAnswerBody
import com.example.ui.theme.premiumAnswerBorder
import com.example.ui.theme.premiumAnswerTitle
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumQuestionBg
import com.example.ui.theme.premiumQuestionBody
import com.example.ui.theme.premiumQuestionBorder
import com.example.ui.theme.premiumQuestionTitle
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary
import com.example.util.PdfGeneratorUtil

@Composable
fun PdfViewerModal(
  item: StudyItem,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  var isDownloaded by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.92f)
        .clip(RoundedCornerShape(26.dp)),
      color = premiumBackground(),
      shape = RoundedCornerShape(26.dp),
      tonalElevation = 8.dp,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
      ) {
        // Premium gradient top header bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BrandGradient)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
          Box(
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .size(110.dp)
              .background(Color.White.copy(alpha = 0.08f), CircleShape),
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f, fill = false),
            ) {
              GradientIconTile(
                icon = Icons.Default.PictureAsPdf,
                gradient = RoseGradient,
                contentDescription = "PDF",
                size = 44.dp,
                cornerRadius = 13.dp,
                iconSize = 23.dp,
              )
              Column {
                Text(
                  text = "PDF Document Preview",
                  fontSize = 16.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                )
                Text(
                  text = "${item.fileSizeKb} KB • A4 Print-Ready",
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFFD6E4FF),
                )
              }
            }

            IconButton(
              onClick = onDismiss,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.16f))
                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(18.dp),
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // PDF Sheet Canvas (Printable A4 look)
        Card(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .border(1.dp, premiumBorder(), RoundedCornerShape(16.dp)),
          colors = CardDefaults.cardColors(containerColor = premiumCard()),
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(20.dp),
          ) {
            // Printable Header
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(BrandGradient)
                  .padding(horizontal = 12.dp, vertical = 5.dp),
              ) {
                Text(
                  text = "TOWFIK EXCLUSIVE STUDY HUB",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  letterSpacing = 0.8.sp,
                  textAlign = TextAlign.Center,
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "WBBSE MADHYAMIK EXAMINATION 2026",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = premiumTextSecondary(),
              )
              Text(
                text = "Class 10 • Subject: ${item.subject} • Chapter: ${item.chapterName.ifBlank { "All Topics" }}",
                fontSize = 11.5.sp,
                color = premiumTextTertiary(),
              )
              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = premiumBorder(), thickness = 1.5.dp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Metadata
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = premiumTextPrimary(),
                modifier = Modifier.weight(1f),
              )
              Spacer(modifier = Modifier.width(8.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (dark) Color(0xFF3A2C10) else Color(0xFFFEF3C7))
                  .border(
                    1.dp,
                    if (dark) Color(0xFF6B531B) else Color(0xFFFDE68A),
                    RoundedCornerShape(8.dp),
                  )
                  .padding(horizontal = 9.dp, vertical = 5.dp),
              ) {
                Text(
                  text = "${item.marks} Marks",
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (dark) Color(0xFFFDE68A) else Color(0xFF92400E),
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
                    answer = item.answer,
                  ),
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
                  verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  // Question Box (Pink)
                  if (qa.question.isNotBlank()) {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(premiumQuestionBg())
                        .border(1.dp, premiumQuestionBorder(), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    ) {
                      Column {
                        Text(
                          text = "$qNum. প্রশ্ন (Question) [${qa.marks} Marks]:",
                          fontSize = 12.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = premiumQuestionTitle(),
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                          text = qa.question,
                          fontSize = 12.5.sp,
                          color = premiumQuestionBody(),
                          lineHeight = 18.sp,
                        )
                      }
                    }
                  }

                  // Answer Box (Blue)
                  if (qa.answer.isNotBlank()) {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(premiumAnswerBg())
                        .border(1.dp, premiumAnswerBorder(), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    ) {
                      Column {
                        Text(
                          text = "$qNum. উত্তর ও সমাধান (Model Answer):",
                          fontSize = 12.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = premiumAnswerTitle(),
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                          text = qa.answer,
                          fontSize = 12.5.sp,
                          color = premiumAnswerBody(),
                          lineHeight = 18.5.sp,
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
                color = premiumTextSecondary(),
              )
              Text(
                text = item.summaryNotes,
                fontSize = 11.5.sp,
                color = premiumTextTertiary(),
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp),
              )
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = premiumBorder())
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Published by Towfik Exclusive Education • Madhyamik 2026 Batch",
              fontSize = 10.sp,
              color = premiumTextTertiary(),
              textAlign = TextAlign.Center,
              modifier = Modifier.fillMaxWidth(),
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Button(
            onClick = {
              isDownloaded = true
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("pdf_download_confirm_btn"),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (isDownloaded) PremiumSuccess600 else TowfikPrimaryBlue,
            ),
            shape = RoundedCornerShape(13.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
          ) {
            Icon(
              imageVector = if (isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
              contentDescription = "Download",
              tint = Color.White,
              modifier = Modifier.size(17.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isDownloaded) "Downloaded ✓" else "Download (${item.fileSizeKb} KB)",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
            )
          }

          OutlinedButton(
            onClick = {
              PdfGeneratorUtil.sharePdf(context, item)
            },
            modifier = Modifier
              .weight(0.8f)
              .height(48.dp)
              .testTag("pdf_share_btn"),
            shape = RoundedCornerShape(13.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, TowfikPrimaryBlue),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = TowfikPrimaryBlue,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Share",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TowfikPrimaryBlue,
            )
          }
        }
      }
    }
  }
}

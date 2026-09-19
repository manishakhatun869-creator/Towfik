package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.model.SubjectItem
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.GradientIconTile
import com.example.ui.theme.PremiumGradientButton
import com.example.ui.theme.PremiumSuccess600
import com.example.ui.theme.SuccessGradient
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumAnswerBg
import com.example.ui.theme.premiumAnswerBorder
import com.example.ui.theme.premiumAnswerTitle
import com.example.ui.theme.premiumBackground
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumCard
import com.example.ui.theme.premiumQuestionBg
import com.example.ui.theme.premiumQuestionBorder
import com.example.ui.theme.premiumQuestionTitle
import com.example.ui.theme.premiumTextPrimary
import com.example.ui.theme.premiumTextSecondary
import com.example.ui.theme.premiumTextTertiary

@Composable
fun AddEditMaterialDialog(
  initialItem: StudyItem? = null,
  availableSubjects: List<SubjectItem> = emptyList(),
  onSave: (StudyItem) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dark = isSystemInDarkTheme()
  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue
  val isEditing = initialItem != null

  var title by remember { mutableStateOf(initialItem?.title ?: "") }
  var classLevel by remember { mutableStateOf(initialItem?.classLevel ?: "Madhyamik 10") }
  var subject by remember { mutableStateOf(initialItem?.subject ?: "Physical Science") }
  var customSubjectName by remember { mutableStateOf("") }
  var isCustomSubjectSelected by remember { mutableStateOf(false) }
  var chapterName by remember { mutableStateOf(initialItem?.chapterName ?: "") }
  var overallMarks by remember { mutableStateOf(initialItem?.marks ?: 3) }
  var itemType by remember { mutableStateOf(initialItem?.type ?: "Suggestion 2026") }
  var isSuggestion2026 by remember { mutableStateOf(initialItem?.isSuggestion2026 ?: true) }
  var pyqYear by remember { mutableStateOf(initialItem?.pyqYear ?: "2025") }
  var summaryNotes by remember { mutableStateOf(initialItem?.summaryNotes ?: "") }

  // Dynamic, unlimited Question & Answer list
  val questionsList = remember {
    mutableStateListOf<QuestionAnswer>().apply {
      if (initialItem?.qaList != null && initialItem.qaList.isNotEmpty()) {
        addAll(initialItem.qaList)
      } else if (initialItem != null && (initialItem.question.isNotBlank() || initialItem.answer.isNotBlank())) {
        add(
          QuestionAnswer(
            qNo = "Q1",
            marks = initialItem.marks,
            question = initialItem.question,
            answer = initialItem.answer,
          ),
        )
      } else {
        add(
          QuestionAnswer(
            qNo = "Q1",
            marks = 3,
            question = "",
            answer = "",
          ),
        )
      }
    }
  }

  val classes = listOf("Madhyamik 10", "Class 9", "Class 11", "Class 12")
  val defaultSubjectNames = if (availableSubjects.isNotEmpty()) {
    availableSubjects.map { it.displayName }
  } else {
    listOf("Physical Science", "Life Science", "Mathematics", "History", "Geography", "Bengali", "English")
  }
  val types = listOf("Suggestion 2026", "PYQ Solved", "Notes & Chapter", "Important Q&A")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.94f)
        .clip(RoundedCornerShape(26.dp)),
      color = premiumBackground(),
      shape = RoundedCornerShape(26.dp),
      tonalElevation = 8.dp,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(14.dp),
      ) {
        // Premium gradient dialog header
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(19.dp))
            .background(BrandGradient)
            .padding(horizontal = 14.dp, vertical = 13.dp),
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
                icon = Icons.Default.CloudUpload,
                gradient = androidx.compose.ui.graphics.Brush.linearGradient(
                  listOf(Color.White.copy(alpha = 0.32f), Color.White.copy(alpha = 0.12f)),
                ),
                contentDescription = "Cloud Upload",
                size = 46.dp,
                cornerRadius = 14.dp,
                iconSize = 24.dp,
              )
              Column {
                Text(
                  text = if (isEditing) "Edit Chapter & Questions" else "Add Chapter & Questions",
                  fontSize = 16.5.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White,
                )
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                  Box(
                    modifier = Modifier
                      .size(7.dp)
                      .clip(CircleShape)
                      .background(Color(0xFF4ADE80)),
                  )
                  Text(
                    text = "Firestore Live Sync • ${questionsList.size} Q&As",
                    fontSize = 11.5.sp,
                    color = Color(0xFFD6E4FF),
                    fontWeight = FontWeight.SemiBold,
                  )
                }
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

        // Scrollable Form Container
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          // SECTION 1: Chapter & General Details
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = premiumCard()),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              EditorSectionLabel(number = "1", title = "Chapter & Topic Details")

              OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Material / Chapter Title (Bengali/English)") },
                placeholder = { Text("e.g. আলোর প্রতিসরণ ও লেন্স সম্পর্কিত প্রশ্নোত্তর (Suggestion 2026)") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("admin_input_title"),
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = accent,
                  unfocusedBorderColor = premiumBorder(),
                  focusedLabelColor = accent,
                ),
              )

              OutlinedTextField(
                value = chapterName,
                onValueChange = { chapterName = it },
                label = { Text("Chapter / Unit Name") },
                placeholder = { Text("e.g. আলো (Light) & Optics") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("admin_input_chapter"),
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = accent,
                  unfocusedBorderColor = premiumBorder(),
                  focusedLabelColor = accent,
                ),
              )

              // Class Selector
              Column {
                Text(
                  text = "Class Level",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = premiumTextSecondary(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  classes.forEach { cls ->
                    FilterChip(
                      selected = classLevel == cls,
                      onClick = { classLevel = cls },
                      label = { Text(cls, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                      colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TowfikPrimaryBlue,
                        selectedLabelColor = Color.White,
                        containerColor = premiumCard(),
                        labelColor = premiumTextSecondary(),
                      ),
                      border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = classLevel == cls,
                        borderColor = premiumBorder(),
                        selectedBorderColor = TowfikPrimaryBlue,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 1.dp,
                      ),
                    )
                  }
                }
              }

              // Subject Selector
              Column {
                Text(
                  text = "Subject",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = premiumTextSecondary(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  defaultSubjectNames.take(4).forEach { subj ->
                    FilterChip(
                      selected = subject == subj && !isCustomSubjectSelected,
                      onClick = {
                        subject = subj
                        isCustomSubjectSelected = false
                      },
                      label = { Text(subj, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                      colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TowfikPrimaryBlue,
                        selectedLabelColor = Color.White,
                        containerColor = premiumCard(),
                        labelColor = premiumTextSecondary(),
                      ),
                    )
                  }
                }
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  defaultSubjectNames.drop(4).forEach { subj ->
                    FilterChip(
                      selected = subject == subj && !isCustomSubjectSelected,
                      onClick = {
                        subject = subj
                        isCustomSubjectSelected = false
                      },
                      label = { Text(subj, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                      colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TowfikPrimaryBlue,
                        selectedLabelColor = Color.White,
                        containerColor = premiumCard(),
                        labelColor = premiumTextSecondary(),
                      ),
                    )
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                  value = if (isCustomSubjectSelected) customSubjectName else subject,
                  onValueChange = {
                    customSubjectName = it
                    subject = it
                    isCustomSubjectSelected = true
                  },
                  label = { Text("Custom Subject Name (Editable)") },
                  placeholder = { Text("e.g. Computer Science, Work Education...") },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(13.dp),
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accent,
                    unfocusedBorderColor = premiumBorder(),
                    focusedLabelColor = accent,
                  ),
                )
              }

              // Category Type Selector
              Column {
                Text(
                  text = "Category Type",
                  fontSize = 12.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = premiumTextSecondary(),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  types.forEach { t ->
                    FilterChip(
                      selected = itemType == t,
                      onClick = {
                        itemType = t
                        isSuggestion2026 = t.contains("Suggestion", ignoreCase = true)
                      },
                      label = { Text(t, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                      colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TowfikPrimaryBlue,
                        selectedLabelColor = Color.White,
                        containerColor = premiumCard(),
                        labelColor = premiumTextSecondary(),
                      ),
                    )
                  }
                }
              }
            }
          }

          // SECTION 2: DYNAMIC QUESTIONS & ANSWERS
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = premiumCard()),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, accent.copy(alpha = 0.35f)),
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Column(modifier = Modifier.weight(1f, fill = false)) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                  ) {
                    Box(
                      modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(SuccessGradient),
                      contentAlignment = Alignment.Center,
                    ) {
                      Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp),
                      )
                    }
                    Text(
                      text = "Questions & Answers (${questionsList.size})",
                      fontSize = 14.5.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = premiumTextPrimary(),
                    )
                  }
                  Text(
                    text = "Add as many questions as needed for this chapter",
                    fontSize = 11.5.sp,
                    color = premiumTextTertiary(),
                    modifier = Modifier.padding(start = 34.dp, top = 2.dp),
                  )
                }

                Button(
                  onClick = {
                    val nextIndex = questionsList.size + 1
                    questionsList.add(
                      QuestionAnswer(
                        qNo = "Q$nextIndex",
                        marks = 2,
                        question = "",
                        answer = "",
                      ),
                    )
                    Toast.makeText(context, "Added Question Q$nextIndex", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = PremiumSuccess600),
                  shape = RoundedCornerShape(11.dp),
                  elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                  modifier = Modifier
                    .height(40.dp)
                    .testTag("admin_add_question_btn"),
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Question",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("+ Add", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
              }

              HorizontalDivider(color = premiumBorder(), thickness = 1.dp)

              questionsList.forEachIndexed { index, qa ->
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, premiumBorder(), RoundedCornerShape(15.dp)),
                  shape = RoundedCornerShape(15.dp),
                  colors = CardDefaults.cardColors(
                    containerColor = if (dark) Color(0xFF0E1730) else Color(0xFFF8FAFD),
                  ),
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                  ) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                      ) {
                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrandGradient)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                          Text(
                            text = "Question #${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                          )
                        }

                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (dark) Color(0xFF3A2C10) else Color(0xFFFEF3C7))
                            .padding(horizontal = 7.dp, vertical = 4.dp),
                        ) {
                          Text(
                            text = "${qa.marks} Marks",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (dark) Color(0xFFFDE68A) else Color(0xFFB45309),
                          )
                        }
                      }

                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                      ) {
                        if (index > 0) {
                          IconButton(
                            onClick = {
                              val itemToMove = questionsList.removeAt(index)
                              questionsList.add(index - 1, itemToMove)
                            },
                            modifier = Modifier.size(30.dp),
                          ) {
                            Icon(
                              imageVector = Icons.Default.ArrowUpward,
                              contentDescription = "Move Up",
                              tint = premiumTextTertiary(),
                              modifier = Modifier.size(15.dp),
                            )
                          }
                        }

                        if (index < questionsList.size - 1) {
                          IconButton(
                            onClick = {
                              val itemToMove = questionsList.removeAt(index)
                              questionsList.add(index + 1, itemToMove)
                            },
                            modifier = Modifier.size(30.dp),
                          ) {
                            Icon(
                              imageVector = Icons.Default.ArrowDownward,
                              contentDescription = "Move Down",
                              tint = premiumTextTertiary(),
                              modifier = Modifier.size(15.dp),
                            )
                          }
                        }

                        IconButton(
                          onClick = {
                            val duplicated = qa.copy(qNo = "${qa.qNo} (Copy)")
                            questionsList.add(index + 1, duplicated)
                            Toast.makeText(context, "Question duplicated", Toast.LENGTH_SHORT).show()
                          },
                          modifier = Modifier.size(30.dp),
                        ) {
                          Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Duplicate question",
                            tint = accent,
                            modifier = Modifier.size(15.dp),
                          )
                        }

                        IconButton(
                          onClick = {
                            if (questionsList.size > 1) {
                              questionsList.removeAt(index)
                              Toast.makeText(context, "Question #${index + 1} removed", Toast.LENGTH_SHORT).show()
                            } else {
                              questionsList[0] = QuestionAnswer("Q1", 2, "", "")
                              Toast.makeText(context, "Question cleared", Toast.LENGTH_SHORT).show()
                            }
                          },
                          modifier = Modifier.size(30.dp),
                        ) {
                          Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete question",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(16.dp),
                          )
                        }
                      }
                    }

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      OutlinedTextField(
                        value = qa.qNo,
                        onValueChange = { newQNo ->
                          questionsList[index] = qa.copy(qNo = newQNo)
                        },
                        label = { Text("Label (Q1, 1a, 2b)", fontSize = 11.sp) },
                        modifier = Modifier.weight(0.9f),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                          focusedBorderColor = accent,
                          unfocusedBorderColor = premiumBorder(),
                        ),
                        singleLine = true,
                      )

                      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(1, 2, 3, 5, 8).forEach { m ->
                          Box(
                            modifier = Modifier
                              .clip(RoundedCornerShape(8.dp))
                              .clickable {
                                questionsList[index] = qa.copy(marks = m)
                              }
                              .background(if (qa.marks == m) BrandGradient else androidx.compose.ui.graphics.Brush.linearGradient(listOf(premiumBorder(), premiumBorder())))
                              .border(
                                1.dp,
                                if (qa.marks == m) Color.Transparent else premiumBorder(),
                                RoundedCornerShape(8.dp),
                              )
                              .padding(horizontal = 7.dp, vertical = 7.dp),
                          ) {
                            Text(
                              text = "${m}M",
                              fontSize = 11.sp,
                              fontWeight = FontWeight.Bold,
                              color = if (qa.marks == m) Color.White else premiumTextSecondary(),
                            )
                          }
                        }
                      }
                    }

                    OutlinedTextField(
                      value = qa.question,
                      onValueChange = { newQuestion ->
                        questionsList[index] = qa.copy(question = newQuestion)
                      },
                      label = { Text("প্রশ্ন (Question Text in Bengali / English)") },
                      placeholder = { Text("যেমনঃ আলোর প্রতিসরণ কাকে বলে? এর দুটি সূত্র লেখো।") },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(11.dp),
                      colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = premiumQuestionTitle(),
                        unfocusedBorderColor = premiumQuestionBorder(),
                        focusedContainerColor = premiumQuestionBg(),
                        unfocusedContainerColor = premiumQuestionBg(),
                        focusedLabelColor = premiumQuestionTitle(),
                      ),
                      maxLines = 4,
                    )

                    OutlinedTextField(
                      value = qa.answer,
                      onValueChange = { newAnswer ->
                        questionsList[index] = qa.copy(answer = newAnswer)
                      },
                      label = { Text("উত্তর ও পূর্ণাঙ্গ সমাধান (Model Answer & Solution)") },
                      placeholder = { Text("যেমনঃ আলোকরশ্মি যখন এক স্বচ্ছ মাধ্যম থেকে অন্য স্বচ্ছ মাধ্যমে প্রবেশ করে...") },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(11.dp),
                      colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = premiumAnswerTitle(),
                        unfocusedBorderColor = premiumAnswerBorder(),
                        focusedContainerColor = premiumAnswerBg(),
                        unfocusedContainerColor = premiumAnswerBg(),
                        focusedLabelColor = premiumAnswerTitle(),
                      ),
                      maxLines = 6,
                    )
                  }
                }
              }

              OutlinedButton(
                onClick = {
                  val nextIndex = questionsList.size + 1
                  questionsList.add(
                    QuestionAnswer(
                      qNo = "Q$nextIndex",
                      marks = 2,
                      question = "",
                      answer = "",
                    ),
                  )
                  Toast.makeText(context, "Added Question Q$nextIndex", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(44.dp),
                shape = RoundedCornerShape(11.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PremiumSuccess600),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PremiumSuccess600.copy(alpha = 0.5f)),
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Add",
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("+ Add Another Question to Chapter", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          // SECTION 3: Summary / Key Formulas (Optional)
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = premiumCard()),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, premiumBorder()),
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              EditorSectionLabel(number = "3", title = "Chapter Summary & Key Formulas (Optional)")
              OutlinedTextField(
                value = summaryNotes,
                onValueChange = { summaryNotes = it },
                label = { Text("Key Exam Points / Formulas / Revision Notes") },
                placeholder = { Text("• সূত্র ১: sin i / sin r = μ (স্নেলের সূত্র)\n• সূত্র ২: 1/f = 1/v - 1/u") },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(88.dp),
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = accent,
                  unfocusedBorderColor = premiumBorder(),
                  focusedLabelColor = accent,
                ),
                maxLines = 3,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Premium gradient save button
        Box(modifier = Modifier.testTag("admin_save_material_btn")) {
          PremiumGradientButton(
            text = if (isEditing) "Update & Sync ${questionsList.size} Questions" else "Save & Sync ${questionsList.size} Questions",
            icon = Icons.Default.Save,
            onClick = {
              if (title.isBlank()) {
                Toast.makeText(context, "Please enter a material / chapter title first!", Toast.LENGTH_SHORT).show()
                return@PremiumGradientButton
              }

              val cleanQaList = questionsList
                .filter { it.question.isNotBlank() || it.answer.isNotBlank() }
                .ifEmpty {
                  listOf(
                    QuestionAnswer(
                      qNo = "Q1",
                      marks = 2,
                      question = questionsList.firstOrNull()?.question ?: "",
                      answer = questionsList.firstOrNull()?.answer ?: "",
                    ),
                  )
                }

              val firstQa = cleanQaList.firstOrNull()
              val computedMarks = if (cleanQaList.isNotEmpty()) {
                cleanQaList.maxOfOrNull { it.marks } ?: overallMarks
              } else {
                overallMarks
              }

              val sanitizedTitle = (if (chapterName.isNotBlank()) "${chapterName}_$title" else title)
                .lowercase()
                .replace(Regex("[^a-zA-Z0-9_]"), "_")
                .take(30)
                .trim('_')
                .ifBlank { "chapter" }

              val autoDocId = initialItem?.id ?: "${sanitizedTitle}_${System.currentTimeMillis()}"

              val newItem = StudyItem(
                id = autoDocId,
                title = title.trim(),
                classLevel = classLevel,
                subject = subject.trim(),
                chapterName = chapterName.trim(),
                type = itemType,
                marks = computedMarks,
                isExclusive = true,
                isSuggestion2026 = isSuggestion2026,
                pyqYear = pyqYear,
                question = firstQa?.question ?: "",
                answer = firstQa?.answer ?: "",
                summaryNotes = summaryNotes.trim(),
                dateAdded = initialItem?.dateAdded ?: "25 Aug 2026",
                fileSizeKb = initialItem?.fileSizeKb ?: (220..380).random(),
                author = "Towfik Sir Exclusive Admin",
                qaList = cleanQaList,
              )

              onSave(newItem)
              Toast.makeText(
                context,
                "Success! Saved '${newItem.title.take(24)}' with ${cleanQaList.size} questions to Firestore!",
                Toast.LENGTH_LONG,
              ).show()
            },
          )
        }
      }
    }
  }
}

@Composable
private fun EditorSectionLabel(number: String, title: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(BrandGradient),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = number,
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
        color = Color.White,
      )
    }
    Text(
      text = title,
      fontSize = 13.5.sp,
      fontWeight = FontWeight.ExtraBold,
      color = premiumTextPrimary(),
    )
  }
}

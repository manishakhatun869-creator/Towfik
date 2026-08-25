package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.theme.TowfikAccentGold
import com.example.ui.theme.TowfikPrimaryBlue

@Composable
fun AddEditMaterialDialog(
    initialItem: StudyItem? = null,
    availableSubjects: List<SubjectItem> = emptyList(),
    onSave: (StudyItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                        answer = initialItem.answer
                    )
                )
            } else {
                // Default 1st question template
                add(
                    QuestionAnswer(
                        qNo = "Q1",
                        marks = 3,
                        question = "",
                        answer = ""
                    )
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
    val marksList = listOf(1, 2, 3, 4, 5, 8)
    val types = listOf("Suggestion 2026", "PYQ Solved", "Notes & Chapter", "Important Q&A")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.95f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color(0xFFF8FAFC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Cloud Upload",
                                tint = TowfikPrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isEditing) "Edit Chapter & Questions" else "Add Chapter & Questions",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF16A34A))
                                )
                                Text(
                                    text = "Cloud Firestore Live Sync • ${questionsList.size} Q&As",
                                    fontSize = 11.sp,
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(17.dp))
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

                // Scrollable Form Container
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // SECTION 1: Chapter & General Details
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "1. Chapter & Topic Details",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TowfikPrimaryBlue
                            )

                            // Title
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Material / Chapter Title (Bengali/English)") },
                                placeholder = { Text("e.g. আলোর প্রতিসরণ ও লেন্স সম্পর্কিত প্রশ্নোত্তর (Suggestion 2026)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_input_title"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TowfikPrimaryBlue)
                            )

                            // Chapter Name
                            OutlinedTextField(
                                value = chapterName,
                                onValueChange = { chapterName = it },
                                label = { Text("Chapter / Unit Name") },
                                placeholder = { Text("e.g. আলো (Light) & Optics") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_input_chapter"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // Class Selector
                            Column {
                                Text(
                                    text = "Class Level:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF475569)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    classes.forEach { cls ->
                                        FilterChip(
                                            selected = classLevel == cls,
                                            onClick = { classLevel = cls },
                                            label = { Text(cls, fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = TowfikPrimaryBlue,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }

                            // Subject Selector
                            Column {
                                Text(
                                    text = "Subject:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF475569)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    defaultSubjectNames.take(4).forEach { subj ->
                                        FilterChip(
                                            selected = subject == subj && !isCustomSubjectSelected,
                                            onClick = {
                                                subject = subj
                                                isCustomSubjectSelected = false
                                            },
                                            label = { Text(subj, fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = TowfikPrimaryBlue,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    defaultSubjectNames.drop(4).forEach { subj ->
                                        FilterChip(
                                            selected = subject == subj && !isCustomSubjectSelected,
                                            onClick = {
                                                subject = subj
                                                isCustomSubjectSelected = false
                                            },
                                            label = { Text(subj, fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = TowfikPrimaryBlue,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = if (isCustomSubjectSelected) customSubjectName else subject,
                                    onValueChange = {
                                        customSubjectName = it
                                        subject = it
                                        isCustomSubjectSelected = true
                                    },
                                    label = { Text("Custom Subject Name (Editable)") },
                                    placeholder = { Text("e.g. Computer Science, Work Education...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            // Category Type Selector
                            Column {
                                Text(
                                    text = "Category Type:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF475569)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    types.forEach { t ->
                                        FilterChip(
                                            selected = itemType == t,
                                            onClick = {
                                                itemType = t
                                                isSuggestion2026 = t.contains("Suggestion", ignoreCase = true)
                                            },
                                            label = { Text(t, fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = TowfikPrimaryBlue,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 2: DYNAMIC QUESTIONS & ANSWERS IN THIS CHAPTER (Add Lots of Questions)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Section 2 Header with Total Count and Add Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Quiz,
                                            contentDescription = "Questions",
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "2. Questions & Answers (${questionsList.size})",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                    }
                                    Text(
                                        text = "Add as many questions as needed for this chapter",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                // Prominent + Add Question Button
                                Button(
                                    onClick = {
                                        val nextIndex = questionsList.size + 1
                                        questionsList.add(
                                            QuestionAnswer(
                                                qNo = "Q$nextIndex",
                                                marks = 2,
                                                question = "",
                                                answer = ""
                                            )
                                        )
                                        Toast.makeText(context, "Added Question Q$nextIndex", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(38.dp)
                                        .testTag("admin_add_question_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Question",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+ Add Question", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                            // Render Each Question & Answer Item in clean Cards
                            questionsList.forEachIndexed { index, qa ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Header of this question item
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color(0xFF0F172A))
                                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                                ) {
                                                    Text(
                                                        text = "Question #${index + 1}",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color(0xFFFEF3C7))
                                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                                ) {
                                                    Text(
                                                        text = "${qa.marks} Marks",
                                                        fontSize = 10.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFB45309)
                                                    )
                                                }
                                            }

                                            // Action Buttons for this question: Duplicate, Move Up, Move Down, Delete
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                // Move Up
                                                if (index > 0) {
                                                    IconButton(
                                                        onClick = {
                                                            val itemToMove = questionsList.removeAt(index)
                                                            questionsList.add(index - 1, itemToMove)
                                                        },
                                                        modifier = Modifier.size(28.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.ArrowUpward,
                                                            contentDescription = "Move Up",
                                                            tint = Color(0xFF64748B),
                                                            modifier = Modifier.size(15.dp)
                                                        )
                                                    }
                                                }

                                                // Move Down
                                                if (index < questionsList.size - 1) {
                                                    IconButton(
                                                        onClick = {
                                                            val itemToMove = questionsList.removeAt(index)
                                                            questionsList.add(index + 1, itemToMove)
                                                        },
                                                        modifier = Modifier.size(28.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.ArrowDownward,
                                                            contentDescription = "Move Down",
                                                            tint = Color(0xFF64748B),
                                                            modifier = Modifier.size(15.dp)
                                                        )
                                                    }
                                                }

                                                // Duplicate Question
                                                IconButton(
                                                    onClick = {
                                                        val duplicated = qa.copy(qNo = "${qa.qNo} (Copy)")
                                                        questionsList.add(index + 1, duplicated)
                                                        Toast.makeText(context, "Question duplicated", Toast.LENGTH_SHORT).show()
                                                    },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ContentCopy,
                                                        contentDescription = "Duplicate question",
                                                        tint = TowfikPrimaryBlue,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                }

                                                // Delete Question (if more than 1, or clear if 1)
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
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete question",
                                                        tint = Color(0xFFDC2626),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }

                                        // Q Number & Marks row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = qa.qNo,
                                                onValueChange = { newQNo ->
                                                    questionsList[index] = qa.copy(qNo = newQNo)
                                                },
                                                label = { Text("Question Label / No (e.g. Q1, 1a, 2b)") },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp)
                                            )

                                            // Marks selector chips for this question
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                listOf(1, 2, 3, 5, 8).forEach { m ->
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(if (qa.marks == m) Color(0xFFB45309) else Color(0xFFE2E8F0))
                                                            .padding(horizontal = 7.dp, vertical = 6.dp)
                                                            .clickable {
                                                                questionsList[index] = qa.copy(marks = m)
                                                            }
                                                    ) {
                                                        Text(
                                                            text = "${m}M",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (qa.marks == m) Color.White else Color(0xFF334155)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Question Text Box (Pink Box highlight)
                                        OutlinedTextField(
                                            value = qa.question,
                                            onValueChange = { newQuestion ->
                                                questionsList[index] = qa.copy(question = newQuestion)
                                            },
                                            label = { Text("প্রশ্ন (Question Text in Bengali / English)") },
                                            placeholder = { Text("যেমনঃ আলোর প্রতিসরণ কাকে বলে? এর দুটি সূত্র লেখো।") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color(0xFFDB2777),
                                                unfocusedBorderColor = Color(0xFFFBCFE8),
                                                focusedContainerColor = Color(0xFFFDF2F8),
                                                unfocusedContainerColor = Color(0xFFFFF1F2)
                                            ),
                                            maxLines = 4
                                        )

                                        // Answer Text Box (Light Blue Box highlight)
                                        OutlinedTextField(
                                            value = qa.answer,
                                            onValueChange = { newAnswer ->
                                                questionsList[index] = qa.copy(answer = newAnswer)
                                            },
                                            label = { Text("উত্তর ও পূর্ণাঙ্গ সমাধান (Model Answer & Solution)") },
                                            placeholder = { Text("যেমনঃ আলোকরশ্মি যখন এক স্বচ্ছ মাধ্যম থেকে অন্য স্বচ্ছ মাধ্যমে প্রবেশ করে...") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color(0xFF0284C7),
                                                unfocusedBorderColor = Color(0xFFBAE6FD),
                                                focusedContainerColor = Color(0xFFF0F9FF),
                                                unfocusedContainerColor = Color(0xFFF0F9FF)
                                            ),
                                            maxLines = 6
                                        )
                                    }
                                }
                            }

                            // Secondary Bottom "+ Add Another Question" Button
                            OutlinedButton(
                                onClick = {
                                    val nextIndex = questionsList.size + 1
                                    questionsList.add(
                                        QuestionAnswer(
                                            qNo = "Q$nextIndex",
                                            marks = 2,
                                            question = "",
                                            answer = ""
                                        )
                                    )
                                    Toast.makeText(context, "Added Question Q$nextIndex", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF059669))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("+ Add Another Question to Chapter", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // SECTION 3: Summary / Key Formulas (Optional)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "3. Chapter Summary & Key Formulas (Optional)",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TowfikPrimaryBlue
                            )
                            OutlinedTextField(
                                value = summaryNotes,
                                onValueChange = { summaryNotes = it },
                                label = { Text("Key Exam Points / Formulas / Revision Notes") },
                                placeholder = { Text("• সূত্র ১: sin i / sin r = μ (স্নেলের সূত্র)\n• সূত্র ২: 1/f = 1/v - 1/u") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                shape = RoundedCornerShape(10.dp),
                                maxLines = 3
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Save Button with Toast Verification
                Button(
                    onClick = {
                        if (title.isBlank()) {
                            Toast.makeText(context, "Please enter a material / chapter title first!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        // Clean and filter questions list
                        val cleanQaList = questionsList
                            .filter { it.question.isNotBlank() || it.answer.isNotBlank() }
                            .ifEmpty {
                                listOf(
                                    QuestionAnswer(
                                        qNo = "Q1",
                                        marks = 2,
                                        question = questionsList.firstOrNull()?.question ?: "",
                                        answer = questionsList.firstOrNull()?.answer ?: ""
                                    )
                                )
                            }

                        // Determine primary question/answer for backwards compatibility
                        val firstQa = cleanQaList.firstOrNull()
                        val computedMarks = if (cleanQaList.isNotEmpty()) {
                            cleanQaList.maxOfOrNull { it.marks } ?: overallMarks
                        } else {
                            overallMarks
                        }

                        // Format document ID to be recognizable in Firebase Console (e.g. madhyamik_light_laws_of_reflection_1740478000)
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
                            qaList = cleanQaList
                        )

                        onSave(newItem)
                        Toast.makeText(
                            context,
                            "Success! Saved '${newItem.title.take(24)}' with ${cleanQaList.size} questions to Firestore!",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_save_material_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = TowfikPrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isEditing) "Update & Sync ${questionsList.size} Questions to Firestore" else "Save & Sync ${questionsList.size} Questions to Firestore",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

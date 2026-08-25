package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreRepository
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.model.SubjectItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: FirestoreRepository = FirestoreRepository(application.applicationContext)

    val items: StateFlow<List<StudyItem>> = repository.items
    val subjects: StateFlow<List<SubjectItem>> = repository.subjects
    val syncStatus: StateFlow<String> = repository.syncStatus
    val isAdminLoggedIn: StateFlow<Boolean> = repository.isAdminLoggedIn
    val savedPdfIds: StateFlow<Set<String>> = repository.savedPdfIds

    val searchQuery = MutableStateFlow("")
    val selectedClass = MutableStateFlow("All Classes")
    val selectedSubject = MutableStateFlow("All Subjects")
    val selectedType = MutableStateFlow("All Materials")
    val selectedMarksFilter = MutableStateFlow(0) // 0 means all

    val streakCount = MutableStateFlow(4)
    val userXp = MutableStateFlow(180)
    val notificationsActive = MutableStateFlow(true)

    val selectedItemForDetail = MutableStateFlow<StudyItem?>(null)
    val selectedItemForPdf = MutableStateFlow<StudyItem?>(null)
    val showAddEditDialog = MutableStateFlow(false)
    val editingItem = MutableStateFlow<StudyItem?>(null)
    val showNotificationAlert = MutableStateFlow(false)
    val showWebPortalDialog = MutableStateFlow(false)

    val webAppUrl = "https://ais-pre-5zfsassdynut36rj53le4n-696515565693.asia-southeast1.run.app"

    // Filtered items for Search and Lists
    val filteredItems: StateFlow<List<StudyItem>> = combine(
        items,
        searchQuery,
        selectedClass,
        selectedSubject,
        selectedType
    ) { allItems, query, cls, subj, type ->
        allItems.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.chapterName.contains(query, ignoreCase = true) ||
                    item.subject.contains(query, ignoreCase = true) ||
                    item.question.contains(query, ignoreCase = true) ||
                    item.answer.contains(query, ignoreCase = true) ||
                    item.qaList.any { it.question.contains(query, ignoreCase = true) || it.answer.contains(query, ignoreCase = true) }

            val matchesClass = cls == "All Classes" || item.classLevel.equals(cls, ignoreCase = true)
            val matchesSubj = subj == "All Subjects" || item.subject.equals(subj, ignoreCase = true)
            val matchesType = when (type) {
                "All Materials" -> true
                "Notes & Chapters" -> item.type.contains("Note", ignoreCase = true) || item.summaryNotes.isNotBlank()
                "PYQs & Suggestions" -> item.type.contains("PYQ", ignoreCase = true) || item.type.contains("Suggestion", ignoreCase = true)
                "1 Mark" -> item.marks == 1 || item.qaList.any { it.marks == 1 }
                "2 Marks" -> item.marks == 2 || item.qaList.any { it.marks == 2 }
                "3 Marks" -> item.marks == 3 || item.qaList.any { it.marks == 3 }
                "4 Marks" -> item.marks == 4 || item.qaList.any { it.marks == 4 }
                "5 Marks" -> item.marks == 5 || item.qaList.any { it.marks == 5 }
                "8 Marks" -> item.marks == 8 || item.qaList.any { it.marks == 8 }
                else -> true
            }

            matchesQuery && matchesClass && matchesSubj && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topSuggestions: StateFlow<List<StudyItem>> = items.combine(MutableStateFlow(true)) { allItems, _ ->
        allItems.filter { it.isSuggestion2026 || it.type.contains("Suggestion", ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pyqItems: StateFlow<List<StudyItem>> = items.combine(MutableStateFlow(true)) { allItems, _ ->
        allItems.filter { it.type.contains("PYQ", ignoreCase = true) || it.pyqYear.isNotBlank() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loginAdmin(email: String, pass: String): Boolean {
        return repository.loginAdmin(email, pass)
    }

    fun logoutAdmin() {
        repository.logoutAdmin()
    }

    fun saveStudyItem(item: StudyItem) {
        repository.addItem(item)
    }

    fun deleteStudyItem(id: String) {
        repository.deleteItem(id)
    }

    fun toggleSavedPdf(itemId: String): Boolean {
        return repository.toggleSavedPdf(itemId)
    }

    fun addQuestionToItem(itemId: String, newQa: QuestionAnswer) {
        val current = items.value.find { it.id == itemId } ?: return
        val currentQaList = if (current.qaList.isNotEmpty()) {
            current.qaList
        } else if (current.question.isNotBlank() || current.answer.isNotBlank()) {
            listOf(QuestionAnswer("Q1", current.marks, current.question, current.answer))
        } else {
            emptyList()
        }
        val updatedQaList = currentQaList + newQa
        val updatedItem = current.copy(
            qaList = updatedQaList,
            question = updatedQaList.firstOrNull()?.question ?: "",
            answer = updatedQaList.firstOrNull()?.answer ?: ""
        )
        saveStudyItem(updatedItem)
    }

    fun deleteQuestionFromItem(itemId: String, qIndex: Int) {
        val current = items.value.find { it.id == itemId } ?: return
        if (qIndex in current.qaList.indices) {
            val updatedQaList = current.qaList.toMutableList().apply { removeAt(qIndex) }
            val firstQa = updatedQaList.firstOrNull()
            val updatedItem = current.copy(
                qaList = updatedQaList,
                question = firstQa?.question ?: "",
                answer = firstQa?.answer ?: ""
            )
            saveStudyItem(updatedItem)
        }
    }

    fun saveSubject(subject: SubjectItem) {
        repository.saveSubject(subject)
    }

    fun deleteSubject(subjectId: String) {
        repository.deleteSubject(subjectId)
    }

    fun openItemDetail(item: StudyItem) {
        selectedItemForDetail.value = item
    }

    fun closeItemDetail() {
        selectedItemForDetail.value = null
    }

    fun openPdfPreview(item: StudyItem) {
        selectedItemForPdf.value = item
    }

    fun closePdfPreview() {
        selectedItemForPdf.value = null
    }

    fun openAddEditDialog(item: StudyItem? = null) {
        editingItem.value = item
        showAddEditDialog.value = true
    }

    fun closeAddEditDialog() {
        showAddEditDialog.value = false
        editingItem.value = null
    }
}

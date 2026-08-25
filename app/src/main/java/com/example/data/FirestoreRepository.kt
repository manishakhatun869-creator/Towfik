package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.model.QuestionAnswer
import com.example.model.StudyItem
import com.example.model.SubjectItem
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class FirestoreRepository(private val context: Context? = null) {
    private val prefs: SharedPreferences? = try {
        context?.getSharedPreferences("towfik_exclusive_prefs", Context.MODE_PRIVATE)
    } catch (t: Throwable) {
        Log.e("FirestoreRepository", "SharedPreferences init error", t)
        null
    }

    private val _items = MutableStateFlow<List<StudyItem>>(loadCachedItems())
    val items: StateFlow<List<StudyItem>> = _items.asStateFlow()

    private val _subjects = MutableStateFlow<List<SubjectItem>>(loadCachedSubjects())
    val subjects: StateFlow<List<SubjectItem>> = _subjects.asStateFlow()

    private val _syncStatus = MutableStateFlow("Connecting to Cloud Firestore...")
    val syncStatus: StateFlow<String> = _syncStatus.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(prefs?.getBoolean("is_admin_logged_in", false) ?: false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _savedPdfIds = MutableStateFlow<Set<String>>(loadSavedPdfIds())
    val savedPdfIds: StateFlow<Set<String>> = _savedPdfIds.asStateFlow()

    private var firestore: FirebaseFirestore? = null
    private var itemsListener: ListenerRegistration? = null
    private var subjectsListener: ListenerRegistration? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private fun loadSavedPdfIds(): Set<String> {
        return prefs?.getStringSet("saved_pdf_item_ids", emptySet()) ?: emptySet()
    }

    fun toggleSavedPdf(itemId: String): Boolean {
        val current = _savedPdfIds.value.toMutableSet()
        val isNowSaved = if (current.contains(itemId)) {
            current.remove(itemId)
            false
        } else {
            current.add(itemId)
            true
        }
        _savedPdfIds.value = current
        try {
            prefs?.edit()?.putStringSet("saved_pdf_item_ids", current)?.apply()
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Error saving pdf ids", t)
        }
        return isNowSaved
    }

    fun isPdfSaved(itemId: String): Boolean {
        return _savedPdfIds.value.contains(itemId)
    }

    init {
        try {
            if (context != null) {
                try {
                    if (FirebaseApp.getApps(context).isEmpty()) {
                        try {
                            val options = FirebaseOptions.Builder()
                                .setProjectId("towfik-exclusive-5e420")
                                .setApplicationId("1:789667629871:android:d197d71e17cd1e34c16fda")
                                .setApiKey("AIzaSyCpvzqtJ34tS61rCCFUa2eFaYGIHglepqw")
                                .setStorageBucket("towfik-exclusive-5e420.firebasestorage.app")
                                .build()
                            FirebaseApp.initializeApp(context, options)
                        } catch (t: Throwable) {
                            try {
                                FirebaseApp.initializeApp(context)
                            } catch (t2: Throwable) {
                                Log.w("FirestoreRepository", "Default app init failed: ${t2.message}")
                            }
                        }
                    }
                } catch (t: Throwable) {
                    Log.w("FirestoreRepository", "FirebaseApp check error: ${t.message}")
                }
            }
            firestore = try {
                FirebaseFirestore.getInstance()
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "FirebaseFirestore getInstance error: ${t.message}")
                null
            }

            if (firestore != null) {
                listenToRealtimeUpdates()
                listenToSubjectsUpdates()
                fetchInitialFirestoreData()
            } else {
                _syncStatus.value = "Offline Mode (${_items.value.size} materials ready)"
            }
        } catch (t: Throwable) {
            Log.w("FirestoreRepository", "Firebase initialization fallback: ${t.message}")
            _syncStatus.value = "Offline Mode (${_items.value.size} materials ready)"
        }
    }

    private fun getDefaultSubjects(): List<SubjectItem> {
        return listOf(
            SubjectItem("subj_phy", "Physical Science", "ভৌত বিজ্ঞান", "science"),
            SubjectItem("subj_life", "Life Science", "জীবন বিজ্ঞান", "biotech"),
            SubjectItem("subj_math", "Mathematics", "গণিত", "calculate"),
            SubjectItem("subj_hist", "History", "ইতিহাস", "history_edu"),
            SubjectItem("subj_geo", "Geography", "ভূগোল", "public"),
            SubjectItem("subj_beng", "Bengali", "বাংলা", "menu_book"),
            SubjectItem("subj_eng", "English", "English", "translate")
        )
    }

    private fun loadCachedSubjects(): List<SubjectItem> {
        val raw = prefs?.getString("cached_subjects_json", null) ?: return getDefaultSubjects()
        return try {
            val jsonArray = JSONArray(raw)
            val list = mutableListOf<SubjectItem>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    SubjectItem(
                        id = obj.optString("id", "subj_$i"),
                        displayName = obj.optString("displayName", ""),
                        bengaliName = obj.optString("bengaliName", ""),
                        iconName = obj.optString("iconName", "menu_book"),
                        isCustom = obj.optBoolean("isCustom", false),
                        description = obj.optString("description", "")
                    )
                )
            }
            if (list.isNotEmpty()) list else getDefaultSubjects()
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Failed to parse cached subjects", t)
            getDefaultSubjects()
        }
    }

    private fun saveCachedSubjects(subjects: List<SubjectItem>) {
        try {
            val jsonArray = JSONArray()
            for (subj in subjects) {
                val obj = JSONObject().apply {
                    put("id", subj.id)
                    put("displayName", subj.displayName)
                    put("bengaliName", subj.bengaliName)
                    put("iconName", subj.iconName)
                    put("isCustom", subj.isCustom)
                    put("description", subj.description)
                }
                jsonArray.put(obj)
            }
            prefs?.edit()?.putString("cached_subjects_json", jsonArray.toString())?.apply()
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Failed to save cached subjects", t)
        }
    }

    private fun loadCachedItems(): List<StudyItem> {
        val raw = prefs?.getString("cached_study_materials_json", null) ?: return emptyList()
        return try {
            val jsonArray = JSONArray(raw)
            val list = mutableListOf<StudyItem>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val rawQa = obj.optJSONArray("qaList")
                val qaList = mutableListOf<QuestionAnswer>()
                if (rawQa != null) {
                    for (q in 0 until rawQa.length()) {
                        val qObj = rawQa.getJSONObject(q)
                        qaList.add(
                            QuestionAnswer(
                                qNo = qObj.optString("qNo", "Q${q + 1}"),
                                marks = qObj.optInt("marks", 2),
                                question = qObj.optString("question", ""),
                                answer = qObj.optString("answer", "")
                            )
                        )
                    }
                }
                val primaryQ = obj.optString("question", "")
                val primaryA = obj.optString("answer", "")
                val finalQa = if (qaList.isNotEmpty()) {
                    qaList
                } else if (primaryQ.isNotBlank() || primaryA.isNotBlank()) {
                    listOf(QuestionAnswer("Q1", obj.optInt("marks", 3), primaryQ, primaryA))
                } else {
                    emptyList()
                }

                list.add(
                    StudyItem(
                        id = obj.optString("id", "item_${System.currentTimeMillis()}_$i"),
                        title = obj.optString("title", ""),
                        classLevel = obj.optString("classLevel", "Madhyamik 10"),
                        subject = obj.optString("subject", "Physical Science"),
                        chapterName = obj.optString("chapterName", ""),
                        type = obj.optString("type", "Suggestion 2026"),
                        marks = obj.optInt("marks", 3),
                        isExclusive = obj.optBoolean("isExclusive", true),
                        isSuggestion2026 = obj.optBoolean("isSuggestion2026", true),
                        pyqYear = obj.optString("pyqYear", "2025"),
                        question = primaryQ.ifBlank { finalQa.firstOrNull()?.question ?: "" },
                        answer = primaryA.ifBlank { finalQa.firstOrNull()?.answer ?: "" },
                        summaryNotes = obj.optString("summaryNotes", ""),
                        dateAdded = obj.optString("dateAdded", "25 Aug 2026"),
                        fileSizeKb = obj.optInt("fileSizeKb", 250),
                        author = obj.optString("author", "Towfik Exclusive Admin"),
                        qaList = finalQa
                    )
                )
            }
            list
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Failed to parse cached items", t)
            emptyList()
        }
    }

    private fun saveCachedItems(items: List<StudyItem>) {
        try {
            val jsonArray = JSONArray()
            for (item in items) {
                val obj = JSONObject().apply {
                    put("id", item.id)
                    put("title", item.title)
                    put("classLevel", item.classLevel)
                    put("subject", item.subject)
                    put("chapterName", item.chapterName)
                    put("type", item.type)
                    put("marks", item.marks)
                    put("isExclusive", item.isExclusive)
                    put("isSuggestion2026", item.isSuggestion2026)
                    put("pyqYear", item.pyqYear)
                    put("question", item.question)
                    put("answer", item.answer)
                    put("summaryNotes", item.summaryNotes)
                    put("dateAdded", item.dateAdded)
                    put("fileSizeKb", item.fileSizeKb)
                    put("author", item.author)

                    val qaArr = JSONArray()
                    for (qa in item.qaList) {
                        val qObj = JSONObject().apply {
                            put("qNo", qa.qNo)
                            put("marks", qa.marks)
                            put("question", qa.question)
                            put("answer", qa.answer)
                        }
                        qaArr.put(qObj)
                    }
                    put("qaList", qaArr)
                }
                jsonArray.put(obj)
            }
            prefs?.edit()?.putString("cached_study_materials_json", jsonArray.toString())?.apply()
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Failed to save cached items", t)
        }
    }

    private fun fetchInitialFirestoreData() {
        val db = firestore ?: return
        scope.launch {
            try {
                db.collection("study_materials").get()
                    .addOnSuccessListener { snapshots ->
                        if (snapshots != null && !snapshots.isEmpty) {
                            val list = mutableListOf<StudyItem>()
                            for (doc in snapshots.documents) {
                                try {
                                    val item = docToStudyItem(doc.id, doc.data ?: emptyMap())
                                    list.add(item)
                                } catch (t: Throwable) {
                                    Log.e("FirestoreRepository", "Parsing error on doc: ${doc.id}", t)
                                }
                            }
                            _items.value = list
                            saveCachedItems(list)
                            _syncStatus.value = "Synced with Cloud Firestore (${list.size} materials)"
                        } else if (_items.value.isEmpty()) {
                            _syncStatus.value = "Connected to Firestore (0 materials found)"
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("FirestoreRepository", "Initial fetch failed: ${e.message}")
                        _syncStatus.value = "Live Firestore Active (Offline ready)"
                    }
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "Fetch exception: ${t.message}")
            }
        }
    }

    private fun listenToRealtimeUpdates() {
        val db = firestore ?: return
        try {
            itemsListener = db.collection("study_materials")
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.w("FirestoreRepository", "Listen failed: $error")
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val list = mutableListOf<StudyItem>()
                        for (doc in snapshots.documents) {
                            try {
                                val item = docToStudyItem(doc.id, doc.data ?: emptyMap())
                                list.add(item)
                            } catch (t: Throwable) {
                                Log.e("FirestoreRepository", "Error parsing doc: ${doc.id}", t)
                            }
                        }
                        if (list.isNotEmpty() || snapshots.documents.isEmpty()) {
                            _items.value = list
                            saveCachedItems(list)
                            _syncStatus.value = "Synced with Cloud Firestore (${list.size} materials)"
                        }
                    }
                }
        } catch (t: Throwable) {
            Log.w("FirestoreRepository", "Error in realtime snapshot: ${t.message}")
        }
    }

    private fun listenToSubjectsUpdates() {
        val db = firestore ?: return
        try {
            subjectsListener = db.collection("subjects")
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.w("FirestoreRepository", "Subject listen error: $error")
                        return@addSnapshotListener
                    }

                    if (snapshots != null && !snapshots.isEmpty) {
                        val subjs = mutableListOf<SubjectItem>()
                        for (doc in snapshots.documents) {
                            val data = doc.data ?: continue
                            subjs.add(
                                SubjectItem(
                                    id = doc.id,
                                    displayName = data["displayName"] as? String ?: "",
                                    bengaliName = data["bengaliName"] as? String ?: "",
                                    iconName = data["iconName"] as? String ?: "menu_book",
                                    isCustom = data["isCustom"] as? Boolean ?: false,
                                    description = data["description"] as? String ?: ""
                                )
                            )
                        }
                        if (subjs.isNotEmpty()) {
                            _subjects.value = subjs
                            saveCachedSubjects(subjs)
                        }
                    }
                }
        } catch (t: Throwable) {
            Log.w("FirestoreRepository", "Error in subjects snapshot: ${t.message}")
        }
    }

    fun addItem(item: StudyItem) {
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            current[index] = item
        } else {
            current.add(0, item)
        }
        _items.value = current
        saveCachedItems(current)

        // Cloud Firestore sync
        val db = firestore
        if (db != null) {
            try {
                _syncStatus.value = "Saving '${item.title}' to Cloud Firestore..."
                db.collection("study_materials").document(item.id)
                    .set(studyItemToMap(item))
                    .addOnSuccessListener {
                        _syncStatus.value = "Live Firestore Synced: '${item.title.take(24)}' (${item.qaList.size} Q&As)"
                        Log.d("FirestoreRepository", "Successfully synced item ${item.id} (${item.title}) to Firestore")
                    }
                    .addOnFailureListener { e ->
                        _syncStatus.value = "Firestore Sync Notice: ${e.localizedMessage ?: "Check Firestore Rules"}"
                        Log.e("FirestoreRepository", "Firestore write failure for ${item.id}", e)
                    }
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "Add item error: ${t.message}")
                _syncStatus.value = "Sync Exception: ${t.localizedMessage}"
            }
        } else {
            _syncStatus.value = "Saved locally. Firestore reconnecting..."
        }
    }

    fun deleteItem(id: String) {
        val updated = _items.value.filter { it.id != id }
        _items.value = updated
        saveCachedItems(updated)

        firestore?.let { db ->
            try {
                db.collection("study_materials").document(id).delete()
                    .addOnSuccessListener {
                        _syncStatus.value = "Deleted material from Cloud Firestore"
                    }
                    .addOnFailureListener { e ->
                        _syncStatus.value = "Delete error: ${e.localizedMessage}"
                    }
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "Delete error: ${t.message}")
            }
        }
    }

    fun saveSubject(subject: SubjectItem) {
        val current = _subjects.value.toMutableList()
        val index = current.indexOfFirst { it.id == subject.id || it.displayName.equals(subject.displayName, ignoreCase = true) }
        if (index >= 0) {
            current[index] = subject
        } else {
            current.add(subject)
        }
        _subjects.value = current
        saveCachedSubjects(current)

        firestore?.let { db ->
            try {
                db.collection("subjects").document(subject.id).set(
                    mapOf(
                        "id" to subject.id,
                        "displayName" to subject.displayName,
                        "bengaliName" to subject.bengaliName,
                        "iconName" to subject.iconName,
                        "isCustom" to subject.isCustom,
                        "description" to subject.description
                    )
                ).addOnSuccessListener {
                    _syncStatus.value = "Subject saved: ${subject.displayName}"
                }
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "Save subject error: ${t.message}")
            }
        }
    }

    fun deleteSubject(subjectId: String) {
        val updated = _subjects.value.filter { it.id != subjectId }
        _subjects.value = updated
        saveCachedSubjects(updated)

        firestore?.let { db ->
            try {
                db.collection("subjects").document(subjectId).delete()
                    .addOnSuccessListener {
                        _syncStatus.value = "Subject removed from Firestore"
                    }
            } catch (t: Throwable) {
                Log.w("FirestoreRepository", "Delete subject error: ${t.message}")
            }
        }
    }

    fun loginAdmin(email: String, pass: String): Boolean {
        if (email.trim().equals("towfik@gmail.com", ignoreCase = true) && pass.trim() == "238890") {
            _isAdminLoggedIn.value = true
            try {
                prefs?.edit()?.putBoolean("is_admin_logged_in", true)?.apply()
            } catch (t: Throwable) {
                Log.e("FirestoreRepository", "Error saving admin login state", t)
            }
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        try {
            prefs?.edit()?.putBoolean("is_admin_logged_in", false)?.apply()
        } catch (t: Throwable) {
            Log.e("FirestoreRepository", "Error saving admin logout state", t)
        }
    }

    private fun studyItemToMap(item: StudyItem): Map<String, Any> {
        val qaMaps = item.qaList.map { qa ->
            mapOf(
                "qNo" to qa.qNo,
                "marks" to qa.marks,
                "question" to qa.question,
                "answer" to qa.answer
            )
        }
        return mapOf(
            "id" to item.id,
            "title" to item.title,
            "classLevel" to item.classLevel,
            "subject" to item.subject,
            "chapterName" to item.chapterName,
            "type" to item.type,
            "marks" to item.marks,
            "isExclusive" to item.isExclusive,
            "isSuggestion2026" to item.isSuggestion2026,
            "pyqYear" to item.pyqYear,
            "question" to item.question,
            "answer" to item.answer,
            "summaryNotes" to item.summaryNotes,
            "dateAdded" to item.dateAdded,
            "fileSizeKb" to item.fileSizeKb,
            "author" to item.author,
            "qaList" to qaMaps
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun docToStudyItem(id: String, map: Map<String, Any>): StudyItem {
        val rawQaList = map["qaList"] as? List<Map<String, Any>> ?: emptyList()
        val qaList = rawQaList.map { qaMap ->
            QuestionAnswer(
                qNo = qaMap["qNo"] as? String ?: "Q1",
                marks = (qaMap["marks"] as? Long)?.toInt() ?: (qaMap["marks"] as? Int) ?: 2,
                question = qaMap["question"] as? String ?: "",
                answer = qaMap["answer"] as? String ?: ""
            )
        }

        val primaryQuestion = map["question"] as? String ?: ""
        val primaryAnswer = map["answer"] as? String ?: ""
        val finalQaList = if (qaList.isNotEmpty()) {
            qaList
        } else if (primaryQuestion.isNotBlank() || primaryAnswer.isNotBlank()) {
            listOf(
                QuestionAnswer(
                    qNo = "Q1",
                    marks = (map["marks"] as? Long)?.toInt() ?: (map["marks"] as? Int) ?: 3,
                    question = primaryQuestion,
                    answer = primaryAnswer
                )
            )
        } else {
            emptyList()
        }

        return StudyItem(
            id = map["id"] as? String ?: id,
            title = map["title"] as? String ?: "",
            classLevel = map["classLevel"] as? String ?: "Madhyamik 10",
            subject = map["subject"] as? String ?: "Physical Science",
            chapterName = map["chapterName"] as? String ?: "",
            type = map["type"] as? String ?: "Suggestion 2026",
            marks = (map["marks"] as? Long)?.toInt() ?: (map["marks"] as? Int) ?: 3,
            isExclusive = map["isExclusive"] as? Boolean ?: true,
            isSuggestion2026 = map["isSuggestion2026"] as? Boolean ?: true,
            pyqYear = map["pyqYear"] as? String ?: "2025",
            question = primaryQuestion.ifBlank { finalQaList.firstOrNull()?.question ?: "" },
            answer = primaryAnswer.ifBlank { finalQaList.firstOrNull()?.answer ?: "" },
            summaryNotes = map["summaryNotes"] as? String ?: "",
            dateAdded = map["dateAdded"] as? String ?: "25 Aug 2026",
            fileSizeKb = (map["fileSizeKb"] as? Long)?.toInt() ?: (map["fileSizeKb"] as? Int) ?: 250,
            author = map["author"] as? String ?: "Towfik Exclusive Admin",
            qaList = finalQaList
        )
    }
}

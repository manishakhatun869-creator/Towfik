package com.example.model

enum class ClassLevel(val displayName: String, val code: String) {
    CLASS_10("Madhyamik 10", "class_10"),
    CLASS_9("Class 9", "class_9"),
    CLASS_11("Class 11", "class_11"),
    CLASS_12("Class 12", "class_12");

    companion object {
        fun fromCode(code: String): ClassLevel {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) || it.displayName.equals(code, ignoreCase = true) } ?: CLASS_10
        }
    }
}

enum class Subject(val displayName: String, val bengaliName: String, val iconName: String) {
    PHYSICAL_SCIENCE("Physical Science", "ভৌত বিজ্ঞান", "science"),
    LIFE_SCIENCE("Life Science", "জীবন বিজ্ঞান", "biotech"),
    MATHEMATICS("Mathematics", "গণিত", "calculate"),
    HISTORY("History", "ইতিহাস", "history_edu"),
    GEOGRAPHY("Geography", "ভূগোল", "public"),
    BENGALI("Bengali", "বাংলা", "menu_book"),
    ENGLISH("English", "ইংরেজি", "translate");

    companion object {
        fun fromString(str: String): Subject {
            return entries.firstOrNull { 
                it.displayName.equals(str, ignoreCase = true) || 
                it.bengaliName.equals(str, ignoreCase = true) ||
                it.name.equals(str, ignoreCase = true)
            } ?: PHYSICAL_SCIENCE
        }
    }
}

enum class ItemType(val displayName: String) {
    SUGGESTION("Suggestion 2026"),
    PYQ("PYQ Solved"),
    CHAPTER_NOTE("Notes & Chapter"),
    IMPORTANT_QA("Important Q&A")
}

data class QuestionAnswer(
    val qNo: String = "Q1",
    val marks: Int = 2,
    val question: String = "",
    val answer: String = ""
)

data class SubjectItem(
    val id: String = "",
    val displayName: String = "",
    val bengaliName: String = "",
    val iconName: String = "menu_book",
    val isCustom: Boolean = false,
    val description: String = ""
)

data class StudyItem(
    val id: String = "",
    val title: String = "",
    val classLevel: String = "Madhyamik 10",
    val subject: String = "Physical Science",
    val chapterName: String = "",
    val type: String = "Suggestion 2026",
    val marks: Int = 3,
    val isExclusive: Boolean = true,
    val isSuggestion2026: Boolean = false,
    val pyqYear: String = "",
    val question: String = "",
    val answer: String = "",
    val qaList: List<QuestionAnswer> = emptyList(),
    val summaryNotes: String = "",
    val dateAdded: String = "15 Aug 2026",
    val fileSizeKb: Int = 277,
    val author: String = "Towfik Sir"
)

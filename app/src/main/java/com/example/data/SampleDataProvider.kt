package com.example.data

import com.example.model.StudyItem

object SampleDataProvider {
    // Pure empty list - All study items and suggestions are fetched live from Firebase Firestore
    fun getInitialStudyItems(): List<StudyItem> {
        return emptyList()
    }
}

package com.emognition.emognition.model.faq

import com.emognition.emognition.infrastructure.asFlow
import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.model.faq.FaqEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FaqEntryRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getAll(): Flow<List<FaqEntry>> = firestore
        .collection("faq")
        .asFlow()
        .map { querySnapshot ->
            querySnapshot.documents.mapNotNull {
                val id = it.id
                val question = it.getString("question") ?: return@mapNotNull null
                val answer = it.getString("answer") ?: return@mapNotNull null
                FaqEntry(id, question, answer)
            }
        }
}
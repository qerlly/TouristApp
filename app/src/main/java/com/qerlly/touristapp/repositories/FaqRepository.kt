package com.qerlly.touristapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.asFlow
import com.qerlly.touristapp.model.FaqModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FaqRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getAll(): Flow<List<FaqModel>> = firestore
        .collection("questions")
        .asFlow()
        .map {
            it.documents.mapNotNull { document ->
                val id = document.id
                val question = document.getString("question") ?: return@mapNotNull null
                val answer = document.getString("answer") ?: return@mapNotNull null
                FaqModel(id, question, answer)
            }
        }
}
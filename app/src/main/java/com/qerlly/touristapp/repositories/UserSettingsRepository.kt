package com.qerlly.touristapp.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.asFlow
import com.qerlly.touristapp.model.UserSettingsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun getByUserID(userId: String): Flow<UserSettingsModel?> = firestore
        .collection("preferences")
        .document(userId)
        .asFlow()
        .filterNotNull()
        .map { document ->
            val id = document.id
            val fullName = document.getString("full_name") ?: return@map null
            val phone = document.getString("phone") ?: return@map null
            UserSettingsModel(id, fullName, phone)
        }

    fun saveByUserID(userId: String, name: String, phone: String) {
        val dateToSave: Map<String, String?> =
            mapOf("full_name" to name, "phone" to phone)
        val doc: DocumentReference = firestore
            .document("preferences/$userId")
        doc.set(dateToSave)
    }
}
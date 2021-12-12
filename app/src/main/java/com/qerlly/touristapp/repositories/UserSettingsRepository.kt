package com.qerlly.touristapp.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.asFlow
import com.qerlly.touristapp.model.UserSettingsModel
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.services.UserAuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authService: UserAuthService,
    private val settingsService: SettingsService,
) {
    fun getByUserID(userId: String?): Flow<UserSettingsModel?> = firestore
        .collection("preferences")
        .document(userId!!)
        .asFlow()
        .filterNotNull()
        .map { document ->
            val id = document.id
            val fullName = document.getString("full_name") ?: return@map null
            val phone = document.getString("phone") ?: return@map null
            val tour = document.getString("tour") ?: return@map null
            UserSettingsModel(id, fullName, phone, tour)
        }

    fun saveByUserID(userId: String, name: String, phone: String, tour: String) {
        val dataToSave: Map<String, String?> =
            mapOf("full_name" to name, "phone" to phone, "tour" to tour)
        val doc: DocumentReference = firestore
            .document("preferences/$userId")
        doc.set(dataToSave)
    }

    fun saveTour(id: String) {
        runBlocking {
            settingsService.setTour(id)
        }
        val dataToSave: Map<String, String?> = mapOf("tour" to id)
        val doc: DocumentReference = firestore.document("preferences/${authService.userId}")
        doc.update(dataToSave)

        val userLocationToSave: Map<String, String?> =
            mapOf("email" to authService.userEmail, "latitude" to "", "longitude" to "")
        val docLoc: DocumentReference = firestore
            .document("tours/$id/members/${authService.userId}")
        docLoc.set(userLocationToSave)
    }

    fun updateUserLocation(latitude: String, longitude: String) {
        var tour: String
        runBlocking { tour = settingsService.getTour().first() }
        val dataToSave: Map<String, String?> =
            mapOf("email" to authService.userEmail, "latitude" to latitude, "longitude" to longitude)
        val doc: DocumentReference = firestore.document("tours/$tour/members/${authService.userId}")
        doc.update(dataToSave)
    }

    fun createUserDoc(userId: String) {
        val dateToSave: Map<String, String?> =
            mapOf("full_name" to "", "phone" to "", "tour" to "")
        val doc: DocumentReference = firestore
            .document("preferences/$userId")
        doc.set(dateToSave)
    }
}
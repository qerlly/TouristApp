package com.qerlly.touristapp.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.asFlow
import com.qerlly.touristapp.model.MemberPoint
import com.qerlly.touristapp.model.NewModel
import com.qerlly.touristapp.model.TourModel
import com.qerlly.touristapp.model.TourPoint
import com.qerlly.touristapp.services.SettingsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TourRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val settingsService: SettingsService
) {

    fun getByTourID(): Flow<TourModel> {
        var tour: String
        runBlocking { tour = settingsService.getTour().first() }
        return firestore
            .collection("tours")
            .document(tour)
            .asFlow()
            .mapNotNull { document ->
                val id = document.id
                val description = document.getString("description") ?: return@mapNotNull null
                val title = document.getString("title") ?: return@mapNotNull null
                val gid = document.getString("gid") ?: return@mapNotNull null
                val time = document.getString("time") ?: return@mapNotNull null
                val password = document.getString("pin") ?: return@mapNotNull null
                val image = document.getString("image") ?: return@mapNotNull null
                TourModel(
                    id = id,
                    title = title,
                    description = description,
                    gid = gid,
                    time = time,
                    password = password,
                    image = image,
                )
            }
    }

    fun getTourAnnouncements(): Flow<List<NewModel>> {
        var tour: String
        runBlocking { tour = settingsService.getTour().first() }
        return firestore
            .collection("tours/$tour/announcements")
            .asFlow()
            .filterNotNull()
            .map {
                it.documents.mapNotNull { document ->
                    val id = document.id
                    val info = document.getString("info") ?: return@mapNotNull null
                    val priority = document.getBoolean("priority") ?: return@mapNotNull null
                    NewModel(id, info, priority)
                }
            }
    }

    fun getTourPoints(): Flow<List<TourPoint>> {
        var tour: String
        runBlocking { tour = settingsService.getTour().first() }
        return firestore
            .collection("tours/$tour/points")
            .asFlow()
            .filterNotNull()
            .map {
                it.documents.mapNotNull { document ->
                    val id = document.id
                    val title = document.getString("title") ?: return@mapNotNull null
                    val description = document.getString("description") ?: return@mapNotNull null
                    val image = document.getString("image") ?: return@mapNotNull null
                    val isDone = document.getBoolean("isDone") ?: return@mapNotNull null
                    val latitude = document.getString("latitude") ?: return@mapNotNull null
                    val longitude = document.getString("longitude") ?: return@mapNotNull null
                    TourPoint(id, isDone, latitude, longitude, title, description, image)
                }
            }
    }

    fun getMembersPoints(): Flow<List<MemberPoint>> {
        var tour: String
        runBlocking { tour = settingsService.getTour().first() }
        return firestore
            .collection("tours/$tour/members")
            .asFlow()
            .filterNotNull()
            .map {
                it.documents.mapNotNull { document ->
                    val id = document.id
                    val email = document.getString("email") ?: return@mapNotNull null
                    val latitude = document.getString("latitude") ?: return@mapNotNull null
                    val longitude = document.getString("longitude") ?: return@mapNotNull null
                    MemberPoint(id, email, latitude, longitude)
                }
            }
    }

    fun removeMemberByUserID(userId: String, tour: String) {
        val doc: DocumentReference = firestore.document("tours/$tour/members/$userId")
        doc.delete()
    }
}
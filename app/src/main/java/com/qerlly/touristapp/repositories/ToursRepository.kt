package com.qerlly.touristapp.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.qerlly.touristapp.asFlow
import com.qerlly.touristapp.model.TourModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToursRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    fun getAll(): Flow<List<TourModel>> = firestore
        .collection("tours")
        .asFlow()
        .map {
            it.documents.mapNotNull { document ->
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
}
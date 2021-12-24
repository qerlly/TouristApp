package com.qerlly.touristapp.model

data class TourPoint(
    val id: String,
    val status: Long,
    val latitude: String,
    val longitude: String,
    val title: String,
    val description: String,
    val image: String,
)
package com.qerlly.touristapp.model

data class TourPoint(
    val id: String,
    val isDone: Boolean,
    val latitude: String,
    val longitude: String,
    val title: String,
    val description: String,
    val image: String,
)
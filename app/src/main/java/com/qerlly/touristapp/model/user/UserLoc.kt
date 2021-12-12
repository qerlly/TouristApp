package com.qerlly.touristapp.model.user

data class UserLoc (
    val id: String,
    val lat: Double,
    val long: Double,
    val ifGuide: Boolean,
    val ifSelfLoc: Boolean
)
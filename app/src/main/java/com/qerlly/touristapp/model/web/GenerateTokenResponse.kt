package com.qerlly.touristapp.model.web

data class GenerateTokenResponse(
    val access: String,
    val refresh: String,
    val id: String,
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String
)
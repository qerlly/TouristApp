package com.qerlly.touristapp.model.web

data class RegistrationDataModel(
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val password2: String
)

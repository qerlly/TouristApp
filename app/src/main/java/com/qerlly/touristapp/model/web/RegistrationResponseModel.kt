package com.qerlly.touristapp.model.web

import java.security.acl.LastOwnerException

data class RegistrationResponseModel(
    val email: String,
    val username: String,
    val first_name: String,
    val last_name: String
)
package com.qerlly.touristapp.model

data class MessageModel(
    val email: String,
    val message: String,
    val time: String,
    val isImage: Boolean,
    val own: Boolean
)
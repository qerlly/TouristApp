package com.qerlly.touristapp.model

import com.qerlly.touristapp.model.user.User

data class Message(
    val message: String,
    var sender: User,
    var createdAt: Long,
)
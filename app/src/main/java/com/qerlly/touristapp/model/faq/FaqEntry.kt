package com.qerlly.touristapp.model.faq

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faqs")
data class FaqEntry(
    @PrimaryKey val id: String,
    val question: String,
    val answer: String,
)
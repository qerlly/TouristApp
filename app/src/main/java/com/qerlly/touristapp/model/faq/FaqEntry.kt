package com.qerlly.touristapp.model.faq

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qerlly.touristapp.model.openclose.CloseOpenModel

@Entity(tableName = "questions")
data class FaqEntry(
    @PrimaryKey override var id: String,
    override var textOnOpen: String,
    override var textOnClosed: String,
): CloseOpenModel(id, textOnOpen, textOnClosed)
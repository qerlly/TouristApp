package com.qerlly.touristapp.infrastructure.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.qerlly.touristapp.model.faq.FaqTable

class PopulateFaqTableCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        FaqTable.values().forEach { faq ->
            db.insert("faqs", CONFLICT_REPLACE, ContentValues().apply {
                put("id", faq.id)
                put("question", faq.question)
                put("answer", faq.answer)
            })
        }
    }
}
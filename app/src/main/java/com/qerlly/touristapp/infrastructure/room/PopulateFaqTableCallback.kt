package com.qerlly.touristapp.infrastructure.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.qerlly.touristapp.model.faq.FaqTable

class PopulateFaqTableCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        FaqTable.values().forEach { faq ->
            db.insert("questions", CONFLICT_REPLACE, ContentValues().apply {
                put("id", faq.id)
                put("textOnOpen", faq.question)
                put("textOnClosed", faq.answer)
            })
        }
    }
}
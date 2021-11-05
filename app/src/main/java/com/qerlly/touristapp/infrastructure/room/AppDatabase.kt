package com.qerlly.touristapp.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qerlly.touristapp.infrastructure.room.dao.FaqDao
import com.qerlly.touristapp.model.faq.FaqEntry

@Database(
    entities = [
        FaqEntry::class,
    ], version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun faqDao(): FaqDao

    companion object {
        const val DATABASE_NAME = "tours-database"
    }
}
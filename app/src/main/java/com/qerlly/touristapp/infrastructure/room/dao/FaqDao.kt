package com.qerlly.touristapp.infrastructure.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.qerlly.touristapp.model.faq.FaqEntry
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FaqDao {
    @Query("""SELECT * FROM questions""")
    abstract fun getAll(): Flow<List<FaqEntry>>
}
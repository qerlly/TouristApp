package com.qerlly.touristapp.model.faq

import com.qerlly.touristapp.infrastructure.room.dao.FaqDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FaqEntryRepository @Inject constructor(
    private val faqDao: FaqDao,
) {
    fun getAll(): Flow<List<FaqEntry>> = faqDao.getAll()
}
package com.qerlly.touristapp.hiltModules

import com.qerlly.touristapp.infrastructure.room.AppDatabase
import com.qerlly.touristapp.infrastructure.room.dao.FaqDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomHiltModule {

    @Provides
    @Singleton
    fun provideLogDao(database: AppDatabase): FaqDao = database.faqDao()
}
package com.qerlly.touristapp.infrastructure.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomProductionDatabaseHiltModule {
    @Provides
    @Singleton
    fun provideApplicationDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .openHelperFactory(RequerySQLiteOpenHelperFactory())
            .addCallback(PopulateFaqTableCallback())
            .build()
}
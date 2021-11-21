package com.qerlly.touristapp.infrastructure.android

import android.app.NotificationManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import com.qerlly.touristapp.model.point.PointsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AndroidHiltModule {

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService()!!

    @Provides
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager =
        context.getSystemService()!!

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService()!!

    @Provides
    fun providePointsRepository(@ApplicationContext context: Context): PointsRepository =
        PointsRepository()
}
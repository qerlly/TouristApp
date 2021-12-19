package com.qerlly.touristapp

import android.app.Application
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.services.UserAuthService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TouristApp: Application() {

    @Inject
    lateinit var userAuthService: UserAuthService

    @Inject
    lateinit var settingsService: SettingsService

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
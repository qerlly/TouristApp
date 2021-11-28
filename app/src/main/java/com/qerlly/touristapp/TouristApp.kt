package com.qerlly.touristapp

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.qerlly.touristapp.services.AuthenticationService
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TouristApp: Application() {

    @Inject
    lateinit var settingsService: SettingsService

    @Inject
    lateinit var authService: AuthenticationService

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initServices()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initServices() = GlobalScope.launch {
        supervisorScope {
            launch {
                settingsService.fetchSettings()
            }
            launch {

            }
        }
    }
}
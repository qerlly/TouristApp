package com.qerlly.touristapp.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.qerlly.touristapp.infrastructure.settings.Settings
import com.qerlly.touristapp.infrastructure.settings.UserSettings
import com.qerlly.touristapp.ui.localSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class SettingsService @Inject constructor(
    @ApplicationContext context: Context,
    private val authService: AuthenticationService,
) {

    private val dataStore: DataStore<Preferences> = context.localSettings

    fun getPolarSettings(): Flow<UserSettings> =
        dataStore.data.map { UserSettings.getFromPreferences(it) }

    suspend fun setLocalizationEnabled(enabled: Boolean) =
        setSetting(Settings.LOCALIZATION, enabled)

    suspend fun setMail(mail: String) =
        setSetting(Settings.MAIL, mail)

    suspend fun setFullName(name: String) =
        setSetting(Settings.FULL_NAME, name)

    suspend fun setTel(tel: String) =
        setSetting(Settings.PHONE, tel)

    suspend fun setPassword(pass: String) =
        setSetting(Settings.PASSWORD, pass)


    private suspend fun setSetting(setting: Settings, value: Any) = dataStore.edit { prefs ->
        Settings.values().find { it.preferenceKey.name == setting.preferenceKey.name }
            ?.saveValue(prefs, value)
    }


    suspend fun fetchSettings() {
        Timber.i("Fetching settings")
       //getSettings()

       //dataStore.edit { mutablePreferences ->
       //    mutablePreferences.clear()
       //    snapshot.data?.forEach { (key, value) ->
       //        val setting = RemoteSettings.values().find { it.preferenceKey.name == key }
       //        setting?.saveValue(mutablePreferences, sanitizeFirebaseValueType(value))
       //        if (setting == null) {
       //            Timber.e("Don't know setting $key with value $value")
       //        }
       //    }
       //}
    }
}
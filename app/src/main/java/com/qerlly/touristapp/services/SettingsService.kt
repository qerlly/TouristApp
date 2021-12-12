package com.qerlly.touristapp.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.qerlly.touristapp.userSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class SettingsService @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = context.userSettings

    fun getTour(): Flow<String> =
        dataStore.data.map { UserPreferences.TOUR.getValue(it) as String }

    suspend fun setTour(tour: String) =
        setSetting(UserPreferences.TOUR, tour)

    fun getUserLocalization(): Flow<Boolean> =
        dataStore.data.map { UserPreferences.LOCALIZATION.getValue(it) as Boolean }

    suspend fun setLocalization(state: Boolean) =
        setSetting(UserPreferences.LOCALIZATION, state)

    private suspend fun setSetting(setting: UserPreferences, value: Any) = dataStore.edit { prefs ->
        UserPreferences.values().find { it.preferenceKey.name == setting.preferenceKey.name }
            ?.saveValue(prefs, value)
    }
}

@Suppress("UNCHECKED_CAST")
enum class UserPreferences(
    val preferenceKey: Preferences.Key<out Any>,
    private val defaultValue: Any,
    private val mapper: ((Any) -> Any)? = null,
) {

    LOCALIZATION(booleanPreferencesKey("localization"), true),

    TOUR(stringPreferencesKey("tour"), "");

    fun getValue(preferences: Preferences): Any = (preferences[preferenceKey] ?: defaultValue).let {
        mapper?.invoke(it) ?: it
    }

    fun saveValue(mutablePreferences: MutablePreferences, value: Any) {
        mutablePreferences[preferenceKey as Preferences.Key<Any>] = value
    }
}
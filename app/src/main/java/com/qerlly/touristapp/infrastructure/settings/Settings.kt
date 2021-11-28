package com.qerlly.touristapp.infrastructure.settings

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

@Suppress("UNCHECKED_CAST")
enum class Settings(
    val preferenceKey: Preferences.Key<out Any>,
    private val defaultValue: Any,
    private val mapper: ((Any) -> Any)? = null,
) {

    FULL_NAME(stringPreferencesKey("full_name"), ""),

    PHONE(stringPreferencesKey("phone"), ""),

    MAIL(stringPreferencesKey("mail"), ""),

    PASSWORD(stringPreferencesKey("password"), ""),

    LOCALIZATION(booleanPreferencesKey("localization"), true);

    fun getValueOrDefault(preferences: Preferences): Any = (preferences[preferenceKey] ?: defaultValue).let {
        mapper?.invoke(it) ?: it
    }

    fun getValueOrNull(preferences: Preferences): Any? = (preferences[preferenceKey])?.let {
        mapper?.invoke(it) ?: it
    }

    fun saveValue(mutablePreferences: MutablePreferences, value: Any) {
        mutablePreferences[preferenceKey as Preferences.Key<Any>] = value
    }
}
package com.qerlly.touristapp.infrastructure.settings

import androidx.datastore.preferences.core.Preferences

data class UserSettings(
    val name: String,
    val mail: String,
    val password: String,
    val tel: String,
    val localization: Boolean
) {
    companion object {
        fun getFromPreferences(preferences: Preferences): UserSettings =
            UserSettings(
                name = Settings.FULL_NAME.getValueOrNull(preferences) as String,
                mail = Settings.MAIL.getValueOrNull(preferences) as String,
                password = Settings.PASSWORD.getValueOrNull(preferences) as String,
                tel = Settings.PHONE.getValueOrNull(preferences) as String,
                localization = Settings.LOCALIZATION.getValueOrDefault(preferences) as Boolean
            )
    }
}
package com.qerlly.touristapp.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.layoutInflater: LayoutInflater
    get() = getSystemService()!!

val Context.localSettings: DataStore<Preferences> by preferencesDataStore(name = "local")
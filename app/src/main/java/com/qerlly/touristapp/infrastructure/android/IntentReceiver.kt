package com.qerlly.touristapp.infrastructure.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow

abstract class IntentReceiver<T>(
    @ApplicationContext private val context: Context,
    action: String,
) {
    abstract val state: MutableStateFlow<T>

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            state.value = onIntentReceived(context, intent)
        }
    }

    init {
        val intentFilter = IntentFilter(action)
        context.registerReceiver(receiver, intentFilter)
    }

    protected abstract fun onIntentReceived(context: Context, intent: Intent): T

    fun unregisterReceiver() = context.unregisterReceiver(receiver)
}
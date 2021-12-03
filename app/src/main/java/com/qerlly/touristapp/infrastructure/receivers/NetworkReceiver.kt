package com.qerlly.touristapp.infrastructure.receivers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.qerlly.touristapp.infrastructure.receivers.NetworkState.Companion.fromNetworkProviderState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

class NetworkReceiver @Inject constructor(
    @ApplicationContext context: Context,
    private val connectivityManager: ConnectivityManager,
) : IntentReceiver<NetworkState>(context, ConnectivityManager.CONNECTIVITY_ACTION) {

    override val state = MutableStateFlow(fromNetworkProviderState(getState()))

    override fun onIntentReceived(context: Context, intent: Intent): NetworkState {
        Timber.i("Updating network state from intent receiver")
        return fromNetworkProviderState(getState())
    }

    private fun getState(): Boolean =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
}

enum class NetworkState {
    DISABLED, ENABLED;

    companion object {
        fun fromNetworkProviderState(state: Boolean): NetworkState = when (state) {
            true -> ENABLED
            false -> DISABLED
        }
    }
}
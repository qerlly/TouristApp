package com.qerlly.touristapp.infrastructure.receivers

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.qerlly.touristapp.infrastructure.receivers.LocationState.Companion.fromLocationProviderState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

class LocationStateReceiver @Inject constructor(
    @ApplicationContext context: Context,
    private val locationManager: LocationManager,
) : IntentReceiver<LocationState>(context, LocationManager.PROVIDERS_CHANGED_ACTION) {

    override val state = MutableStateFlow(
        fromLocationProviderState(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    )

    override fun onIntentReceived(context: Context, intent: Intent): LocationState {
        Timber.i("Updating location state from intent receiver")
        return fromLocationProviderState(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }
}

enum class LocationState {
    DISABLED, ENABLED;

    companion object {
        fun fromLocationProviderState(state: Boolean): LocationState = when (state) {
            true -> ENABLED
            false -> DISABLED
        }
    }
}
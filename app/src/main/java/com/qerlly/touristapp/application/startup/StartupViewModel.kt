package com.qerlly.touristapp.application.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.services.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
) : ViewModel() {

  /*  val isUserSignedIn: StateFlow<AuthenticationStatus> =
        authenticationService
            .authenticationStatus
            .map { if (it) AuthenticationStatus.AUTHENTICATED else AuthenticationStatus.NOT_AUTHENTICATED }
            .stateIn(viewModelScope, SharingStarted.Eagerly, AuthenticationStatus.LOADING)*/

    enum class AuthenticationStatus {
        LOADING, AUTHENTICATED, NOT_AUTHENTICATED
    }
}
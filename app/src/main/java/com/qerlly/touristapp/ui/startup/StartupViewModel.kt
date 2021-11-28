package com.qerlly.touristapp.ui.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.services.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
) : ViewModel() {

 /*   val isUserSignedIn: StateFlow<AuthenticationStatus> =
        authenticationService
            .authenticationStatus
            .map { if (it) Authenticated else NotAuthenticated }
            .stateIn(viewModelScope, SharingStarted.Eagerly, Loading)*/

    val loginHasBeenTried = MutableStateFlow(false)

    val isPasswordEmpty = MutableStateFlow(false)

    val isPasswordInvalid = MutableStateFlow(false)

    val isInvalidEmail = MutableStateFlow(false)

    fun onLoginAsync(email: String, password: String): Deferred<Boolean> {
        return viewModelScope.async {
            false
        }
    }

    enum class AuthenticationStatus {
        LOADING, AUTHENTICATED, NOT_AUTHENTICATED
    }
}
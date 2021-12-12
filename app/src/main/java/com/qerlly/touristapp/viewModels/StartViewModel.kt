package com.qerlly.touristapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.qerlly.touristapp.repositories.UserSettingsRepository
import com.qerlly.touristapp.services.UserAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val userAuthService: UserAuthService,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val invalidEmailFormat = MutableStateFlow(false)

    val isPasswordEmpty = MutableStateFlow(false)

    val isPasswordInvalid = MutableStateFlow(false)

    val isPasswordNotMatch = MutableStateFlow(false)

    val isInvalidEmail = MutableStateFlow(false)

    val loginButtonEnabled = MutableStateFlow(true)

    val registrationButtonEnabled = MutableStateFlow(true)

    val isUserLogin: StateFlow<Status> = userAuthService.authenticationStatus
            .map { if (it) Status.LOGIN else Status.NOT_LOGIN }
            .stateIn(viewModelScope, SharingStarted.Eagerly, Status.LOADING)

    fun login(email: String, password: String): Deferred<AuthResult> = viewModelScope.async {
            userAuthService.login(email, password)
        }

    fun register(email: String, password: String): Deferred<AuthResult> = viewModelScope.async {
            userAuthService.register(email, password)
        }

    fun forgetPassword(email: String) = userAuthService.forgetPassword(email)

    fun clearFlags() {
        invalidEmailFormat.value = false
        isPasswordEmpty.value = false
        isPasswordInvalid.value = false
        isInvalidEmail.value = false
        isPasswordNotMatch.value = false
        loginButtonEnabled.value = true
        registrationButtonEnabled.value = true
    }

    fun createUserDoc(uid: String) {
        userSettingsRepository.createUserDoc(uid)
    }

    enum class Status { LOADING, LOGIN, NOT_LOGIN }
}
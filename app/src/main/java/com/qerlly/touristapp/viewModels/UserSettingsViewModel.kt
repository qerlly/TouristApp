package com.qerlly.touristapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.R
import com.qerlly.touristapp.model.UserSettingsModel
import com.qerlly.touristapp.repositories.UserSettingsRepository
import com.qerlly.touristapp.services.SettingsService
import com.qerlly.touristapp.services.UserAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userAuthService: UserAuthService,
    private val userSettingsRepository: UserSettingsRepository,
    private val settingsService: SettingsService,
): ViewModel() {

    val isUserActive = userAuthService.currentUser

    val activeUserEmail = userAuthService.userEmail

    val isPasswordEmpty = MutableStateFlow(false)

    val isPasswordInvalid = MutableStateFlow(false)

    val isPasswordNotMatch = MutableStateFlow(false)

    val dataState: StateFlow<UserSettingsModel> =
        userSettingsRepository.getByUserID(userAuthService.userId!!)
            .filterNotNull()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                UserSettingsModel("", "", "", "")
            )

    val localizationState: StateFlow<Boolean> =
        settingsService
            .getUserLocalization()
            .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun saveUserDate(name: String, phone: String) {
        userSettingsRepository.saveByUserID(userAuthService.userId!!, name, phone)
    }

    fun saveLocalizationState(state: Boolean) {
        viewModelScope.launch { settingsService.setLocalization(state) }
    }

    fun changePassword(context: Context, password: String, newPassword: String) {
        try {
            userAuthService.changePassword(context, activeUserEmail!!, password, newPassword)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.login_unknown_error, Toast.LENGTH_LONG).show()
            Timber.e(e)
        }
    }

    fun logout() { userAuthService.logout() }
}
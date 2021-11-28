package com.qerlly.touristapp.ui.startup

import androidx.lifecycle.ViewModel
import com.qerlly.touristapp.infrastructure.retrofit.MainService
import com.qerlly.touristapp.model.Tour
import com.qerlly.touristapp.model.web.GenerateTokenData
import com.qerlly.touristapp.model.web.GenerateTokenResponse
import com.qerlly.touristapp.model.web.RegistrationDataModel
import com.qerlly.touristapp.model.web.RegistrationRequestModel
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val mainService: MainService,
    private val settingsService: SettingsService,
) : ViewModel() {

    val isUserSignedIn = MutableStateFlow(false)

    val loginHasBeenTried = MutableStateFlow(false)

    val isPasswordEmpty = MutableStateFlow(false)

    val isPasswordInvalid = MutableStateFlow(false)

    val isInvalidEmail = MutableStateFlow(false)

    suspend fun login(generateTokenData: GenerateTokenData = GenerateTokenData("testtest111", "hashedPassword")) {
        mainService.generateToken(generateTokenData).subscribe(object : SingleObserver<List<Tour>> {
            override fun onError(e: Throwable) {
                println("onError")
            }

            override fun onSubscribe(s: Disposable) {
                println("onSubscribe")
            }

            override fun onSuccess(t: List<Tour>) {
                runBlocking { settingsService.fetchSettings() }
                println("success")
            }
        })
    }

    fun register(registrationDataModel: RegistrationDataModel) {
        mainService.registration(registrationDataModel).subscribe(
            object : SingleObserver<RegistrationRequestModel> {
                override fun onError(e: Throwable) {
                    println("onError")
                }

                override fun onSubscribe(s: Disposable) {
                    println("onSubscribe")
                }

                override fun onSuccess(t: RegistrationRequestModel) {
                    runBlocking { settingsService.fetchSettings() }
                    println("success")
                }
            })
    }
}
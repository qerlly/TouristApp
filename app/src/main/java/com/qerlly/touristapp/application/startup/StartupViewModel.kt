package com.qerlly.touristapp.application.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.infrastructure.retrofit.MainService
import com.qerlly.touristapp.model.web.RegistrationDataModel
import com.qerlly.touristapp.model.web.RegistrationResponseModel
import com.qerlly.touristapp.services.AuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Objects.hash
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val mainService: MainService
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

    fun register(){
        val mySubscriber = object: SingleObserver<RegistrationResponseModel> {
            override fun onError(e: Throwable) {
                println("onError")
            }

            override fun onSubscribe(s: Disposable) {
                println("onSubscribe")
            }
            override fun onSuccess(t: RegistrationResponseModel) {
                println("success")
            }
        }
/*        val spec = PBEKeySpec("qwerty123".toCharArray(), "anishchanka".toByteArray(Charsets.UTF_8), 1000, 256)
        var hashedPassword = ""
        try {
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            hashedPassword = skf.generateSecret(spec).encoded.toString()
        } catch (e: Exception){

        }*/
        mainService.registration(
            RegistrationDataModel("aliaksei0011anishchanka@gmail.com",
            "anishchanka112",
            "aliaksei1",
            "anishchanka1",
                "hashedPassword",
                "hashedPassword")).subscribe(mySubscriber)
    }

    enum class AuthenticationStatus {
        LOADING, AUTHENTICATED, NOT_AUTHENTICATED
    }
}
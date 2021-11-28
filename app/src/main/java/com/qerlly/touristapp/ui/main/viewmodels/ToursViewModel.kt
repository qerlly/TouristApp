package com.qerlly.touristapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import com.qerlly.touristapp.infrastructure.retrofit.MainService
import com.qerlly.touristapp.model.Tour
import com.qerlly.touristapp.model.web.GenerateTokenResponse
import com.qerlly.touristapp.model.web.ToursResponse
import com.qerlly.touristapp.services.SettingsService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ToursViewModel @Inject constructor(
    private val mainService: MainService,
    private val settingsService: SettingsService,

) : ViewModel() {
    val tours = MutableStateFlow<List<Tour>?>(null)
    fun getTours(){
        mainService.getTours().subscribe(object : SingleObserver<List<Tour>> {
            override fun onError(e: Throwable) {
                println("onError")
            }

            override fun onSubscribe(s: Disposable) {
                println("onSubscribe")
            }

            override fun onSuccess(t: List<Tour>) {
                tours.value = t
                println("success")
            }
        })
    }
}
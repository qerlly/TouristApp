package com.qerlly.touristapp.infrastructure.retrofit

import com.qerlly.touristapp.model.user.Token
import com.qerlly.touristapp.model.web.RegistrationDataModel
import com.qerlly.touristapp.model.web.RegistrationResponseModel
import io.reactivex.rxjava3.core.Single

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainService {

    @GET("users/{userId}.json")
    suspend fun getUser(@Path("userId") userId: Int): Response<Token>

    @POST("api/user/registration/")
    fun registration(
        @Body registrationModel: RegistrationDataModel
    ): Single<RegistrationResponseModel>
}
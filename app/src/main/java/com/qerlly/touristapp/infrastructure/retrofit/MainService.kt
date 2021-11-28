package com.qerlly.touristapp.infrastructure.retrofit

import com.qerlly.touristapp.model.Tour
import com.qerlly.touristapp.model.user.Token
import com.qerlly.touristapp.model.web.*
import io.reactivex.rxjava3.core.Single

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MainService {

    @GET("users/{userId}.json")
    suspend fun getUser(@Path("userId") userId: Int): Response<Token>

    @GET("api/tour/")
    fun getTours(): Single<List<Tour>>

    @POST("api/user/registration/")
    fun registration(
        @Body registrationModel: RegistrationDataModel
    ): Single<RegistrationRequestModel>

    @POST("api/token/")
    fun generateToken(@Body generateTokenData: GenerateTokenData): Single<List<Tour>>

    @GET("/api/tour/{id}/")
    fun getTour(@Path("id") id: Int): Single<Tour>
}
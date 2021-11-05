package com.qerlly.touristapp.infrastructure.retrofit

import com.qerlly.touristapp.model.user.Token
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MainService {

    @GET("users/{userId}.json")
    suspend fun getUser(@Path("userId") userId : Int) : Response<Token>
}
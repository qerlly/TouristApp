package com.qerlly.touristapp.infrastructure.retrofit

import retrofit2.http.GET

interface MainService {

    @GET("users/{userId}.json")
    suspend fun getUser(@Path("userId") userId : Int) : Response<User>
}
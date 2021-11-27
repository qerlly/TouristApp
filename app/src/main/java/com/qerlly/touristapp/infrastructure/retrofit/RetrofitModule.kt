package com.qerlly.touristapp.infrastructure.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun providesBaseUrl() : String = "http://10.0.2.2:8000/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String) : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .build()
        val build = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
        return build
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit : Retrofit) : MainService = retrofit.create(MainService::class.java)
}
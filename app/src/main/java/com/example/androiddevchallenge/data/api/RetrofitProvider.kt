package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private val climacellRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.CLIMACELL_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ClimacellAuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val googleMapRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_MAP_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(GoogleMapAuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideClimacell(): Retrofit = climacellRetrofit

    fun provideGoogleMap(): Retrofit = googleMapRetrofit
}
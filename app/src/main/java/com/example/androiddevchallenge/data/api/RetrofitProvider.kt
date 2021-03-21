package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.CLIMACELL_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provide(): Retrofit = retrofit
}
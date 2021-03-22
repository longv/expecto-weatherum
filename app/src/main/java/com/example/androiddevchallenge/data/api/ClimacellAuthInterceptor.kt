package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ClimacellAuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
            .newBuilder()
            .addQueryParameter("apikey", BuildConfig.CLIMACELL_API_KEY)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}
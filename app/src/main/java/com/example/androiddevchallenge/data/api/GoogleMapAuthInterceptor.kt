package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GoogleMapAuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url()
            .newBuilder()
            .addQueryParameter("key", BuildConfig.GOOGLE_MAP_API_KEY)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}
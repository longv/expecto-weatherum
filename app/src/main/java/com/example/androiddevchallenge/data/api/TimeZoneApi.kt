package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.data.model.TimeZone
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeZoneApi {

    @GET("timezone/json")
    fun getTimeZone(
        @Query("location", encoded = true) location: String,
        @Query("timestamp") timestamp: Long
    ): Call<TimeZone>
}
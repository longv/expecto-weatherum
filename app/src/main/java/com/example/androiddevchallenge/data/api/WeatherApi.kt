package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.data.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("timelines")
    fun getWeather(
        @Query("location") location: String,
        @Query("fields") fields: String,
        @Query("startTime") startTime: String? = null,
        @Query("endTime") endTime: String? = null,
        @Query("timesteps") timesteps: String = "1h",
        @Query("units") units: String = "metric",
        @Query("timezone") timezone: String? = null,
    ): Call<WeatherData>
}
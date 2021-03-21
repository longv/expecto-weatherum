package com.example.androiddevchallenge.data.repository

import com.example.androiddevchallenge.data.api.RetrofitProvider
import com.example.androiddevchallenge.data.api.WeatherApi
import com.example.androiddevchallenge.data.model.WeatherTimeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherRepository {

    private val api by lazy {
        RetrofitProvider.provide().create(WeatherApi::class.java)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getWeather(lat: Float, lng: Float): Result<List<WeatherTimeline>> =
        withContext(Dispatchers.IO) {
            val response = api.getWeather(
                location = "$lat,$lng",
                fields = "weatherCode,temperature,temperatureApparent,humidity,windSpeed,cloudCover,precipitationProbability,precipitationType,sunriseTime,sunsetTime",
            ).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()?.timelines.orEmpty())
            } else {
                Result.Error(IllegalStateException(response.errorBody()?.string().orEmpty()))
            }
        }
}
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
    suspend fun getWeather(
        lat: Float,
        lng: Float,
        startTime: String,
        endTime: String
    ): Result<WeatherTimeline> =
        withContext(Dispatchers.IO) {
            val response = api.getWeather(
                location = "$lat,$lng",
                fields = "weatherCode,temperature,temperatureApparent,humidity,windSpeed,cloudCover,precipitationProbability,precipitationType",
                startTime = startTime,
                endTime = endTime
            ).execute()
            if (response.isSuccessful) {
                val timeline = response.body()?.data?.timelines?.firstOrNull()
                if (timeline != null) {
                    Result.Success(timeline)
                } else {
                    Result.Error(IllegalStateException("No result found!"))
                }
            } else {
                Result.Error(IllegalStateException(response.errorBody()?.string().orEmpty()))
            }
        }
}
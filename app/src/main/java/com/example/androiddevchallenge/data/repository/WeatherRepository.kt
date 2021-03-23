/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.data.repository

import com.example.androiddevchallenge.data.api.RetrofitProvider
import com.example.androiddevchallenge.data.api.TimeZoneApi
import com.example.androiddevchallenge.data.api.WeatherApi
import com.example.androiddevchallenge.data.model.WeatherTimeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherRepository {

    private val weatherApi by lazy {
        RetrofitProvider.provideClimacell().create(WeatherApi::class.java)
    }

    private val timeZoneApi by lazy {
        RetrofitProvider.provideGoogleMap().create(TimeZoneApi::class.java)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getWeather(
        lat: Float,
        lng: Float
    ): Result<WeatherTimeline> =
        withContext(Dispatchers.IO) {
            val timeZoneResponse = timeZoneApi.getTimeZone(
                location = "$lat,$lng",
                timestamp = System.currentTimeMillis() / 1000
            ).execute()
            val weatherResponse = weatherApi.getWeather(
                location = "$lat,$lng",
                fields = "weatherCode,temperature,temperatureApparent,humidity,windSpeed,cloudCover,precipitationProbability,precipitationType",
                timezone = timeZoneResponse.takeIf { it.isSuccessful }?.body()?.timeZoneId
            ).execute()
            if (weatherResponse.isSuccessful) {
                val timeline = weatherResponse.body()?.data?.timelines?.firstOrNull()
                if (timeline != null) {
                    Result.Success(timeline)
                } else {
                    Result.Error(IllegalStateException("No result found!"))
                }
            } else {
                Result.Error(IllegalStateException(weatherResponse.errorBody()?.string().orEmpty()))
            }
        }
}

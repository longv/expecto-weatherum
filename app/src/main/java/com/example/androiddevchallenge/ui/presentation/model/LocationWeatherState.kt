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
package com.example.androiddevchallenge.ui.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import com.example.androiddevchallenge.R

data class LocationWeatherState(
    val id: String,
    val locationName: String,
    val currentWeather: CurrentWeather,
    val hourlyWeather: List<HourWeather>
)

data class CurrentWeather(
    val temperature: String,
    val temperatureApparent: String,
    @RawRes val anim: Int
)

data class HourWeather(
    val time: @Composable () -> String,
    @DrawableRes val icon: Int,
    val temperature: String
)

val dummyLocationWeatherStates = listOf(
    LocationWeatherState(
        id = "1",
        locationName = "Helsinki",
        currentWeather = CurrentWeather(
            temperature = "1C",
            temperatureApparent = "6C",
            anim = R.raw.lottie_weather_snow_sunny
        ),
        hourlyWeather = listOf(
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            ),
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            ),
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            )
        )
    ),
    LocationWeatherState(
        id = "2",
        locationName = "Ho Chi Minh",
        currentWeather = CurrentWeather(
            temperature = "1C",
            temperatureApparent = "6C",
            anim = R.raw.lottie_weather_snow_sunny
        ),
        hourlyWeather = listOf(
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            ),
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            ),
            HourWeather(
                time = { "Now" },
                icon = R.drawable.ic_weather_snowy,
                temperature = "1C"
            )
        )
    )
)

package com.example.androiddevchallenge.ui.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
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
        id = "1",
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

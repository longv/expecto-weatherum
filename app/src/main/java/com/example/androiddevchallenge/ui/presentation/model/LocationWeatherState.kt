package com.example.androiddevchallenge.ui.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

data class LocationWeatherState(
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

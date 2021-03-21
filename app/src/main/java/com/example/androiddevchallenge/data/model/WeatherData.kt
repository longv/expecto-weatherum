package com.example.androiddevchallenge.data.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("timelines") val timelines: List<WeatherTimeline>
)

package com.example.androiddevchallenge.data.model

import com.google.gson.annotations.SerializedName

data class WeatherTimeline(
    @SerializedName("timestep") val timestep: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("intervals") val intervals: List<WeatherTimelineInterval>
)

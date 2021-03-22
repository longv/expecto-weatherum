package com.example.androiddevchallenge.data.model

import com.example.androiddevchallenge.data.util.TimestampUtils
import com.google.gson.annotations.SerializedName

data class WeatherTimelineInterval(
    @SerializedName("startTime") val startTime: String,
    @SerializedName("values") val values: WeatherValues
) {

    fun isNightTime(): Boolean = TimestampUtils.isNightTime(startTime)
}

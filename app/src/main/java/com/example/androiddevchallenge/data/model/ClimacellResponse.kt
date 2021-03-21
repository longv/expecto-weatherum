package com.example.androiddevchallenge.data.model

import com.google.gson.annotations.SerializedName

data class ClimacellResponse(
    @SerializedName("data") val data: WeatherData
)

package com.example.androiddevchallenge.data.model

import com.google.gson.annotations.SerializedName

data class WeatherValues(
    @SerializedName("weatherCode") val weatherCode: Int,
    @SerializedName("temperature") val temperature: Float,
    @SerializedName("temperatureApparent") val temperatureApparent: Float,
    @SerializedName("humidity") val humidity: Float,
    @SerializedName("windSpeed") val windSpeed: Float,
    @SerializedName("cloudCover") val cloudCover: Float,
    @SerializedName("precipitationProbability") val precipitationProbability: Float,
    @SerializedName("precipitationType") val precipitationTypeCode: Int,
    @SerializedName("sunriseTime") val sunriseTime: Float,
    @SerializedName("sunsetTime") val sunsetTime: Float
) {

    fun getWeatherStatus() = when (weatherCode) {
        1000 -> WeatherType.CLEAR
        1001 -> WeatherType.CLOUDY
        1100 -> WeatherType.MOSTLY_CLEAR
        1101 -> WeatherType.PARTLY_CLOUDY
        1102 -> WeatherType.MOSTLY_CLOUDY
        2000 -> WeatherType.FOG
        2100 -> WeatherType.LIGHT_FOG
        3000 -> WeatherType.LIGHT_WIND
        3001 -> WeatherType.WIND
        3002 -> WeatherType.STRONG_WIND
        4000 -> WeatherType.DRIZZLE
        4001 -> WeatherType.RAIN
        4200 -> WeatherType.LIGHT_RAIN
        4201 -> WeatherType.HEAVY_RAIN
        5000 -> WeatherType.SNOW
        5001 -> WeatherType.FLURRIES
        5100 -> WeatherType.LIGHT_SNOW
        5101 -> WeatherType.HEAVY_SNOW
        6000 -> WeatherType.FREEZING_DRIZZLE
        6001 -> WeatherType.FREEZING_RAIN
        6200 -> WeatherType.LIGHT_FREEZING_RAIN
        6201 -> WeatherType.HEAVY_FREEZING_RAIN
        7000 -> WeatherType.ICE_PELLETS
        7101 -> WeatherType.HEAVY_ICE_PELLETS
        7102 -> WeatherType.LIGHT_ICE_PELLETS
        8000 -> WeatherType.THUNDERSTORM
        else -> WeatherType.UNKNOWN
    }

    fun getPrecipitationType() = when (precipitationTypeCode) {
        1 -> PrecipitationType.RAIN
        2 -> PrecipitationType.SNOW
        3 -> PrecipitationType.FREEZING_RAIN
        4 -> PrecipitationType.ICE_PELLETS
        else -> PrecipitationType.UNKNOWN
    }
}

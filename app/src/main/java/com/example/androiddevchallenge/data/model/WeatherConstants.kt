package com.example.androiddevchallenge.data.model

enum class WeatherType {
    CLEAR,
    CLOUDY,
    MOSTLY_CLEAR,
    PARTLY_CLOUDY,
    MOSTLY_CLOUDY,
    FOG,
    LIGHT_FOG,
    LIGHT_WIND,
    WIND,
    STRONG_WIND,
    DRIZZLE,
    RAIN,
    LIGHT_RAIN,
    HEAVY_RAIN,
    SNOW,
    FLURRIES,
    LIGHT_SNOW,
    HEAVY_SNOW,
    FREEZING_DRIZZLE,
    FREEZING_RAIN,
    LIGHT_FREEZING_RAIN,
    HEAVY_FREEZING_RAIN,
    ICE_PELLETS,
    HEAVY_ICE_PELLETS,
    LIGHT_ICE_PELLETS,
    THUNDERSTORM,
    UNKNOWN;

    override fun toString(): String {
        return super.toString().replace("_", " ").capitalize()
    }
}

enum class PrecipitationType {
    RAIN,
    SNOW,
    FREEZING_RAIN,
    ICE_PELLETS,
    UNKNOWN;

    override fun toString(): String {
        return super.toString().replace("_", " ").capitalize()
    }
}
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
package com.example.androiddevchallenge.ui.presentation

import android.location.Address
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.model.WeatherTimeline
import com.example.androiddevchallenge.data.model.WeatherType
import com.example.androiddevchallenge.data.repository.Result
import com.example.androiddevchallenge.data.repository.WeatherRepository
import com.example.androiddevchallenge.data.util.TimestampUtils
import com.example.androiddevchallenge.ui.presentation.model.CurrentWeather
import com.example.androiddevchallenge.ui.presentation.model.HourWeather
import com.example.androiddevchallenge.ui.presentation.model.LocationWeatherState
import com.example.androiddevchallenge.ui.presentation.model.Message
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> = _message

    private val _state = MutableLiveData<List<LocationWeatherState>>()
    val state: LiveData<List<LocationWeatherState>> = _state

    fun onCurrentLocationProvided(address: Address) {
        loadWeatherForLocation(
            address.latitude.toFloat(),
            address.longitude.toFloat(),
            address.locality
        )
    }

    fun onPlaceSearchSucceeded(place: Place) {
        loadWeatherForLocation(
            place.latLng!!.latitude.toFloat(),
            place.latLng!!.longitude.toFloat(),
            place.name.orEmpty()
        )
    }

    private fun loadWeatherForLocation(
        lat: Float,
        lng: Float,
        placeName: String
    ) {
        viewModelScope.launch {
            val result = WeatherRepository.getWeather(
                lat = lat,
                lng = lng
            )
            when (result) {
                is Result.Success -> {
                    val locationWeatherState = result.value.mapToWeatherState(placeName)
                    _state.value =
                        _state.value.orEmpty().plus(locationWeatherState).distinctBy { it.id }
                }
                is Result.Error -> {
                    _message.value =
                        Message(result.exception.message ?: "An unknown error has occurred!")
                }
            }
        }
    }

    private fun WeatherTimeline.mapToWeatherState(locationName: String) = LocationWeatherState(
        id = locationName,
        locationName = locationName,
        currentWeather = intervals.first().let {
            CurrentWeather(
                temperature = it.values.temperature.toInt().toString().plus(" \u2103"),
                temperatureApparent = it.values.temperatureApparent.toInt().toString()
                    .plus(" \u2103"),
                anim = if (it.isNightTime()) {
                    when (it.values.getWeatherStatus()) {
                        WeatherType.CLEAR,
                        WeatherType.MOSTLY_CLEAR -> R.raw.lottie_weather_night
                        WeatherType.CLOUDY,
                        WeatherType.PARTLY_CLOUDY,
                        WeatherType.MOSTLY_CLOUDY -> R.raw.lottie_weather_cloudynight
                        WeatherType.FOG,
                        WeatherType.LIGHT_FOG -> R.raw.lottie_weather_mist
                        WeatherType.WIND,
                        WeatherType.LIGHT_WIND,
                        WeatherType.STRONG_WIND -> R.raw.lottie_weather_windy
                        WeatherType.DRIZZLE,
                        WeatherType.FREEZING_DRIZZLE,
                        WeatherType.LIGHT_RAIN,
                        WeatherType.RAIN,
                        WeatherType.FREEZING_RAIN -> R.raw.lottie_weather_rainynight
                        WeatherType.HEAVY_RAIN,
                        WeatherType.HEAVY_FREEZING_RAIN -> R.raw.lottie_weather_storm
                        WeatherType.SNOW,
                        WeatherType.FLURRIES,
                        WeatherType.LIGHT_SNOW,
                        WeatherType.HEAVY_SNOW,
                        WeatherType.ICE_PELLETS,
                        WeatherType.HEAVY_ICE_PELLETS,
                        WeatherType.LIGHT_ICE_PELLETS -> R.raw.lottie_weather_snownight
                        WeatherType.THUNDERSTORM -> R.raw.lottie_weather_thunder
                        else -> R.raw.lottie_weather_sunny
                    }
                } else {
                    when (it.values.getWeatherStatus()) {
                        WeatherType.CLEAR,
                        WeatherType.MOSTLY_CLEAR -> R.raw.lottie_weather_sunny
                        WeatherType.CLOUDY,
                        WeatherType.PARTLY_CLOUDY,
                        WeatherType.MOSTLY_CLOUDY -> R.raw.lottie_weather_partly_cloudy
                        WeatherType.FOG,
                        WeatherType.LIGHT_FOG -> R.raw.lottie_weather_mist
                        WeatherType.WIND,
                        WeatherType.LIGHT_WIND,
                        WeatherType.STRONG_WIND -> R.raw.lottie_weather_windy
                        WeatherType.DRIZZLE,
                        WeatherType.FREEZING_DRIZZLE,
                        WeatherType.LIGHT_RAIN,
                        WeatherType.RAIN,
                        WeatherType.FREEZING_RAIN -> R.raw.lottie_weather_partly_shower
                        WeatherType.HEAVY_RAIN,
                        WeatherType.HEAVY_FREEZING_RAIN -> R.raw.lottie_weather_stormshowersday
                        WeatherType.SNOW,
                        WeatherType.FLURRIES,
                        WeatherType.LIGHT_SNOW,
                        WeatherType.HEAVY_SNOW,
                        WeatherType.ICE_PELLETS,
                        WeatherType.HEAVY_ICE_PELLETS,
                        WeatherType.LIGHT_ICE_PELLETS -> R.raw.lottie_weather_snow
                        WeatherType.THUNDERSTORM -> R.raw.lottie_weather_thunder
                        else -> R.raw.lottie_weather_sunny
                    }
                }
            )
        },
        hourlyWeather = intervals.mapIndexed { index, interval ->
            HourWeather(
                time = if (index == 0) {
                    { stringResource(id = R.string.now) }
                } else {
                    { TimestampUtils.getHHStringForDate(interval.startTime) }
                },
                icon = if (interval.isNightTime()) {
                    when (interval.values.getWeatherStatus()) {
                        WeatherType.CLEAR,
                        WeatherType.MOSTLY_CLEAR -> R.drawable.ic_weather_night
                        WeatherType.CLOUDY,
                        WeatherType.PARTLY_CLOUDY,
                        WeatherType.MOSTLY_CLOUDY -> R.drawable.ic_weather_cloudy_night
                        WeatherType.FOG,
                        WeatherType.LIGHT_FOG -> R.drawable.ic_weather_haze
                        WeatherType.WIND,
                        WeatherType.LIGHT_WIND,
                        WeatherType.STRONG_WIND -> R.drawable.ic_weather_windy
                        WeatherType.DRIZZLE,
                        WeatherType.FREEZING_DRIZZLE,
                        WeatherType.LIGHT_RAIN,
                        WeatherType.RAIN,
                        WeatherType.FREEZING_RAIN -> R.drawable.ic_weather_rain_night
                        WeatherType.HEAVY_RAIN,
                        WeatherType.HEAVY_FREEZING_RAIN -> R.drawable.ic_weather_rain_night
                        WeatherType.SNOW,
                        WeatherType.FLURRIES,
                        WeatherType.LIGHT_SNOW,
                        WeatherType.HEAVY_SNOW -> R.drawable.ic_weather_snowy
                        WeatherType.ICE_PELLETS,
                        WeatherType.HEAVY_ICE_PELLETS,
                        WeatherType.LIGHT_ICE_PELLETS -> R.drawable.ic_weather_hail
                        WeatherType.THUNDERSTORM -> R.drawable.ic_weather_thunderstorm_night
                        else -> R.raw.lottie_weather_sunny
                    }
                } else {
                    when (interval.values.getWeatherStatus()) {
                        WeatherType.CLEAR,
                        WeatherType.MOSTLY_CLEAR -> R.drawable.ic_weather_sunny
                        WeatherType.CLOUDY,
                        WeatherType.PARTLY_CLOUDY,
                        WeatherType.MOSTLY_CLOUDY -> R.drawable.ic_weather_cloudy
                        WeatherType.FOG,
                        WeatherType.LIGHT_FOG -> R.drawable.ic_weather_haze
                        WeatherType.WIND,
                        WeatherType.LIGHT_WIND,
                        WeatherType.STRONG_WIND -> R.drawable.ic_weather_windy
                        WeatherType.DRIZZLE,
                        WeatherType.FREEZING_DRIZZLE,
                        WeatherType.LIGHT_RAIN,
                        WeatherType.RAIN,
                        WeatherType.FREEZING_RAIN -> R.drawable.ic_weather_partly_rain
                        WeatherType.HEAVY_RAIN,
                        WeatherType.HEAVY_FREEZING_RAIN -> R.drawable.ic_weather_rain
                        WeatherType.SNOW,
                        WeatherType.FLURRIES,
                        WeatherType.LIGHT_SNOW,
                        WeatherType.HEAVY_SNOW -> R.drawable.ic_weather_snowy
                        WeatherType.ICE_PELLETS,
                        WeatherType.HEAVY_ICE_PELLETS,
                        WeatherType.LIGHT_ICE_PELLETS -> R.drawable.ic_weather_hail
                        WeatherType.THUNDERSTORM -> R.drawable.ic_weather_thunderstorm
                        else -> R.raw.lottie_weather_sunny
                    }
                },
                temperature = interval.values.temperature.toInt().toString().plus(" \u2103")
            )
        }
    )
}

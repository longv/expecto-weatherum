package com.example.androiddevchallenge.ui.presentation

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
import com.example.androiddevchallenge.ui.presentation.model.CurrentWeather
import com.example.androiddevchallenge.ui.presentation.model.HourWeather
import com.example.androiddevchallenge.ui.presentation.model.LocationWeatherState
import com.example.androiddevchallenge.ui.presentation.model.Message
import com.example.androiddevchallenge.data.util.TimestampUtils
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch
import java.util.*

class WeatherViewModel : ViewModel() {

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> = _message

    private val _state = MutableLiveData<LocationWeatherState>()
    val state: LiveData<LocationWeatherState> = _state

    fun loadWeatherData() {
        viewModelScope.launch {
            val result = WeatherRepository.getWeather(
                lat = 60.16724396659218f,
                lng = 24.974772156177295f,
                startTime = TimestampUtils.getISO8601StringForNow(),
                endTime = TimestampUtils.getISO8601StringForDate(Date().apply { time += 24 * 3600 * 1000 })
            )
            when (result) {
                is Result.Success -> {
                    _state.value = result.value.mapToWeatherState("Helsinki")
                }
                is Result.Error -> {
                    _message.value =
                        Message(result.exception.message ?: "An unknown error has occurred!")
                }
            }
        }
    }

    fun onPlaceSearchSucceeded(place: Place) {
       loadWeatherForLocation(place)
    }

    fun onPlaceSearchCancelled() {
        _message.value = Message("Cannot search for places!")
    }

    private fun loadWeatherForLocation(place: Place) {
        viewModelScope.launch {
            val result = WeatherRepository.getWeather(
                lat = place.latLng!!.latitude.toFloat(),
                lng = place.latLng!!.longitude.toFloat(),
                startTime = TimestampUtils.getISO8601StringForNow(),
                endTime = TimestampUtils.getISO8601StringForDate(Date().apply { time += 24 * 3600 * 1000 })
            )
            when (result) {
                is Result.Success -> {
                    _state.value = result.value.mapToWeatherState(place.name.orEmpty())
                }
                is Result.Error -> {
                    _message.value =
                        Message(result.exception.message ?: "An unknown error has occurred!")
                }
            }
        }
    }

    private fun WeatherTimeline.mapToWeatherState(locationName: String) = LocationWeatherState(
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
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

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.BuildConfig
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.presentation.model.*
import com.example.androiddevchallenge.ui.presentation.widget.Pager
import com.example.androiddevchallenge.ui.presentation.widget.PagerState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var geocoder: Geocoder

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            initWeatherForLastKnownLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp(viewModel) {
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN,
                        listOf(
                            Place.Field.ID,
                            Place.Field.NAME,
                            Place.Field.LAT_LNG,
                            Place.Field.UTC_OFFSET
                        )
                    ).build(this)
                    // TODO Replace with new registerForActivityResult
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
                }
            }
        }

        initPlaceSdk()
        initLocationHelpers()
        checkLocationPermission() {
            initWeatherForLastKnownLocation()
        }
    }

    private fun initPlaceSdk() {
        Places.initialize(this, BuildConfig.GOOGLE_MAP_API_KEY)
    }

    private fun initLocationHelpers() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this)
    }

    @SuppressLint("MissingPermission")
    private fun initWeatherForLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses.firstOrNull()?.also { address ->
                viewModel.onCurrentLocationProvided(address)
            }
        }
    }

    private fun checkLocationPermission(doOnGranted: () -> Unit) {
        val locationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            doOnGranted()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.also {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    viewModel.onPlaceSearchSucceeded(place)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: WeatherViewModel, onSearchLocationRequested: () -> Unit = {}) {
    Scaffold(
        content = {
            val weatherStates: List<LocationWeatherState>? by viewModel.state.observeAsState()
            weatherStates?.let {
                AllLocationsWeather(it)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSearchLocationRequested
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_search),
                    contentDescription = "Search location"
                )
            }
        }
    )

    val message: Message? by viewModel.message.observeAsState()
    message?.Read {
        DisplaySnackbar(message = it)
    }
}

@Composable
fun AllLocationsWeather(weatherStates: List<LocationWeatherState>) {
    val pagerState by remember {
        mutableStateOf(PagerState())
    }
    with(pagerState) {
        val newMaxPage = (weatherStates.size - 1).coerceAtLeast(0)
        val maxPageChanged = maxPage != newMaxPage
        maxPage = newMaxPage
        currentPage = if (maxPageChanged) {
            newMaxPage
        } else {
            currentPage
        }
    }

    Pager(
        state = pagerState,
        modifier = Modifier.fillMaxHeight()
    ) {
        weatherStates.getOrNull(currentPage)?.let {
            LocationWeather(it) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    weatherStates.forEachIndexed { index, _ ->
                        Icon(
                            painter = if (index == pagerState.currentPage) {
                                painterResource(id = R.drawable.ic_filled_circle)
                            } else {
                                painterResource(id = R.drawable.ic_outline_circle)
                            },
                            contentDescription = "Pager marker",
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationWeather(
    weatherState: LocationWeatherState,
    headerContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        headerContent()
        Text(
            text = weatherState.locationName,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
        )
        WeatherDisplay(weatherState.currentWeather)
        HourlyWeather(weatherState.hourlyWeather)
    }
}

@Composable
fun WeatherDisplay(currentWeather: CurrentWeather) {
    Column(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.6f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Current degree in Celsius
        Text(text = currentWeather.temperature)
        // Current feel like in Celsius
        Text(
            text = stringResource(
                id = R.string.temperature_apparent_x,
                currentWeather.temperatureApparent
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Animation for current weather
        LottieAnimation(
            spec = LottieAnimationSpec.RawRes(currentWeather.anim),
            animationState = rememberLottieAnimationState(
                autoPlay = true,
                repeatCount = Integer.MAX_VALUE
            ),
            modifier = Modifier
                .fillMaxHeight()
        )
    }
}

@Composable
fun HourlyWeather(hourlyWeather: List<HourWeather>) {
    LazyRow(
        modifier = Modifier.padding(start = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hourlyWeather) { hourWeather ->
            WeatherPerHour(hourWeather = hourWeather)
        }
    }
}

@Composable
fun WeatherPerHour(hourWeather: HourWeather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = hourWeather.time())
        Icon(
            painter = painterResource(id = hourWeather.icon),
            contentDescription = "Weather of a specific hour",
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
        )
        Text(text = hourWeather.temperature)
    }
}

@Composable
fun DisplaySnackbar(message: String) {
    Snackbar {
        Text(text = message)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(WeatherViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(WeatherViewModel())
    }
}

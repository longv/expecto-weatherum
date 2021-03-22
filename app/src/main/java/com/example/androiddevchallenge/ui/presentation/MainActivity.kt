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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.presentation.model.CurrentWeather
import com.example.androiddevchallenge.ui.presentation.model.HourWeather
import com.example.androiddevchallenge.ui.presentation.model.LocationWeatherState
import com.example.androiddevchallenge.ui.presentation.model.Message
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.loadWeatherData()
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: WeatherViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        val weatherState: LocationWeatherState? by viewModel.state.observeAsState()
        weatherState?.let {
            LocationWeather(it)
        }
    }

    val message: Message? by viewModel.message.observeAsState()
    message?.Read {
        DisplaySnackbar(message = it)
    }
}

@Composable
fun LocationWeather(weatherState: LocationWeatherState) {
    Column(
        modifier = Modifier.background(Color.Yellow)
    ) {
        Text(
            text = "Helsinki",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .background(Color.Cyan)
                .fillMaxWidth()
        )
        WeatherDisplay(weatherState.currentWeather)
        HourlyWeather(weatherState.hourlyWeather)
        //DailyWeather()
    }
}

@Composable
fun WeatherDisplay(currentWeather: CurrentWeather) {
    Column(
        modifier = Modifier
            .background(Color.Green)
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
            )
        )
        // Animation for current weather
        LottieAnimation(
            spec = remember { LottieAnimationSpec.RawRes(currentWeather.anim) },
            animationState = rememberLottieAnimationState(
                autoPlay = true,
                repeatCount = Integer.MAX_VALUE
            ),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
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
fun DailyWeather() {
    Column {
        WeatherPerDay()
        WeatherPerDay()
        WeatherPerDay()
        WeatherPerDay()
    }
}

@Composable
fun WeatherPerDay() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Monday")
        Text(text = "Sunny")
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
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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        LocationWeather()
    }
}

@Composable
fun LocationWeather() {
    Column(
        modifier = Modifier.background(Color.Yellow)
    ) {
        Text(
            text = "Helsinki",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Cyan)
                .fillMaxWidth()
        )
        WeatherDisplay()
        HourlyWeather()
        DailyWeather()
    }
}

@Composable
fun WeatherDisplay() {
    Column(
        modifier = Modifier
            .background(Color.Green)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Current degree in Celsius
        Text(text = "29")
        // Animation for current weather
        LottieAnimation(
            spec = remember { LottieAnimationSpec.RawRes(R.raw.lottie_weather_snow_sunny) },
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
fun HourlyWeather() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeatherPerHour()
        WeatherPerHour()
        WeatherPerHour()
        WeatherPerHour()
    }
}

@Composable
fun WeatherPerHour() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Now")
        Icon(
            painter = painterResource(id = R.drawable.ic_weather_sunny),
            contentDescription = "Weather of a specific hour",
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
        )
        Text(text = "12")
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

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

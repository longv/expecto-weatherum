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

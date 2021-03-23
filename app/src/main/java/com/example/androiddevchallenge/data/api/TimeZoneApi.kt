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
package com.example.androiddevchallenge.data.api

import com.example.androiddevchallenge.data.model.TimeZone
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeZoneApi {

    @GET("timezone/json")
    fun getTimeZone(
        @Query("location", encoded = true) location: String,
        @Query("timestamp") timestamp: Long
    ): Call<TimeZone>
}

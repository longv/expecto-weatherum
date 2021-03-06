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
package com.example.androiddevchallenge.data.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimestampUtils {

    private const val ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    fun getISO8601StringForNow(): String {
        val now = Date()
        return getISO8601StringForDate(now)
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date
     * Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    fun getISO8601StringForDate(date: Date): String {
        val dateFormat: DateFormat = SimpleDateFormat(ISO_8601_FORMAT, Locale.US)
        return dateFormat.format(date)
    }

    /**
     * Return an HH-formatted time string for specified date/time
     *
     * @param date
     * String
     * @return String with format "HH"
     */
    fun getHHStringForDate(date: String): String {
        val dateFormat: DateFormat = SimpleDateFormat(ISO_8601_FORMAT, Locale.US)
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(date) ?: Date()
        }
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        return String.format("%02d", hours)
    }

    /**
     * Check if specified date is at night
     *
     * @param date
     * String
     * @return True if specified date is at night
     */
    fun isNightTime(date: String): Boolean {
        val dateFormat: DateFormat = SimpleDateFormat(ISO_8601_FORMAT, Locale.US)
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(date) ?: Date()
        }
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        return hours >= 18 || hours < 6
    }
}

package com.example.androiddevchallenge.data.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimestampUtils {

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
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
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
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
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
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(date) ?: Date()
        }
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        return hours >= 18 || hours < 6
    }
}
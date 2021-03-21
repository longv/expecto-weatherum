package com.example.androiddevchallenge.data.repository

sealed class Result<out T> {

    data class Success<T>(val value: T): Result<T>()

    data class Error(val exception: Exception): Result<Nothing>()
}

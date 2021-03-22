package com.example.androiddevchallenge.ui.presentation.model

import androidx.compose.runtime.Composable

data class Message(
    private val msg: String,
    private val consumeMsg: Boolean = true
) {

    private var isRead: Boolean = false

    @Composable
    fun Read(onRead: @Composable (String) -> Unit) {
        if (consumeMsg) {
            if (!isRead) {
                onRead(msg)
                isRead = true
            }
        } else {
            onRead(msg)
        }
    }
}

package ru.mission.glossary

import kotlinx.coroutines.flow.SharedFlow

internal interface WebViewCallback {

    val sharedFlow: SharedFlow<CallbackModel>

    sealed interface CallbackModel {

        data class BodyReceived(
            val url: String,
            val body: String,
        ) : CallbackModel
    }
}
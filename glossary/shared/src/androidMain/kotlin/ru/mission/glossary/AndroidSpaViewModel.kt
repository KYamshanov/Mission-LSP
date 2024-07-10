package ru.mission.glossary

import kotlinx.coroutines.flow.StateFlow

interface AndroidSpaViewModel {

    val webViewState: StateFlow<Model>

    fun loadUrl(url: String)

    fun onResponseReceived(url: String, body: String)

    fun interceptRequest(url: String): Boolean

    sealed interface Model {

        data object Inactive : Model

        data class LoadUrl(val url: String) : Model

        data class FetchUrl(val url: String) : Model
    }
}
package ru.mission.glossary

import android.webkit.WebView
import kotlinx.coroutines.flow.StateFlow

interface WebViewKeeper {

    val onPageLoadedStateFlow: StateFlow<Page?>?

    val webView: WebView?

    fun setWebView(
        webView: WebView,
        onPageLoadedStateFlow: StateFlow<Page?>,
    )

    fun clear()

    data class Page(
        val url: String,
        val body: String,
    )
} 
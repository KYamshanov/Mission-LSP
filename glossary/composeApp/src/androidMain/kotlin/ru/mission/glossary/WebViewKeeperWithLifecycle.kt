package ru.mission.glossary

import android.webkit.WebView
import kotlinx.coroutines.flow.StateFlow

internal class WebViewKeeperWithLifecycle : WebViewKeeper {

    override var onPageLoadedStateFlow: StateFlow<WebViewKeeper.Page?>? = null
    override var webView: WebView? = null

    override fun setWebView(webView: WebView, onPageLoadedStateFlow: StateFlow<WebViewKeeper.Page?>) {
        this.webView = webView
        this.onPageLoadedStateFlow = onPageLoadedStateFlow
    }

    override fun clear() {
        this.webView = null
        this.onPageLoadedStateFlow = null
    }
}
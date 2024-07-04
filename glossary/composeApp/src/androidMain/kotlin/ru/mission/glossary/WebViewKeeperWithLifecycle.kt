package ru.mission.glossary

import android.webkit.WebView

internal class WebViewKeeperWithLifecycle : WebViewKeeper{
    override var webView: WebView? = null
}
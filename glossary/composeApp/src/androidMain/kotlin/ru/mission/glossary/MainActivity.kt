package ru.mission.glossary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.context.GlobalContext
import ru.mission.glossary.components.factory.RootComponentFactory

class MainActivity : ComponentActivity() {

    private val webViewKeeper = GlobalContext.get().get<WebViewKeeper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = GlobalContext.get().get<RootComponentFactory>().create(
            componentContext = defaultComponentContext(),
        )

        setContent {

            App(root)

            AndroidView(
                modifier = Modifier.alpha(0f),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        val onPageFinishedStateFlow = MutableStateFlow<WebViewKeeper.Page?>(null)
                        addJavascriptInterface(
                            JavaScriptInterface(onPageFinishedStateFlow),
                            "Android"
                        )


                        webViewClient = object : WebViewClient() {

                            var v = true

                            override fun shouldInterceptRequest(
                                view: WebView,
                                request: WebResourceRequest,
                            ): WebResourceResponse? {
                                val url = request.url
                                if (v && url.toString().contains("/props/api/collections")) {
                                    v = false
                                    runOnUiThread {
                                        view.loadUrl(url.toString())
                                    }
                                    return null
                                }

                                return super.shouldInterceptRequest(view, request)
                            }

                            override fun onPageFinished(view: WebView, url: String) {
                                if (url.contains("/props/api/collections")) {
                                    println("D")
                                    view.loadUrl("javascript:window.Android.processContent(document.getElementsByTagName('body')[0].innerText);");
                                }
                                super.onPageFinished(view, url)
                            }
                        }

                        @SuppressLint("SetJavaScriptEnabled")
                        settings.javaScriptEnabled = true
                        webViewKeeper.setWebView(this, onPageFinishedStateFlow)

                    }
                })

            // App(root)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webViewKeeper.clear()
    }
}

class JavaScriptInterface(
    val onPageFinishedStateFlow: MutableStateFlow<WebViewKeeper.Page?>,
) {

    @JavascriptInterface
    fun processContent(aContent: String) {
        onPageFinishedStateFlow.update { WebViewKeeper.Page("/props/api/collections", aContent) }
    }
}
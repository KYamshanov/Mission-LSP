package ru.mission.glossary

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext
import ru.mission.glossary.components.factory.RootComponentFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = GlobalContext.get().get<RootComponentFactory>().create(
            componentContext = defaultComponentContext(),
        )

        setContent {

            val mUrl =
                "https://translate.yandex.ru/subscribe?collection_id=6549257082cf737777c1706b&utm_source=new_collection_share_desktop"

            println("XYI:TETS")

            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )


                    addJavascriptInterface(JavaScriptInterface(), "Android")

                    webViewClient = object : WebViewClient() {

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            runOnUiThread {
                                evaluateJavascript(
                                    "(function() {" +
                                        "var originalFetch = window.fetch;" +
                                        "window.fetch = function(input, init) {" +
                                        "return originalFetch.apply(this, arguments).then(function(response) {" +
                                        "if (response) {" +
                                        // You can customize this part to handle different response types
                                        "response.json().then(function(json) {" +
                                        "Android.onJsonResponse(JSON.stringify(json));" +
                                        "});" +
                                        "}" +
                                        "return response;" +
                                        "});" +
                                        "};" +
                                        "})();",
                                    null
                                );
                            }
                        }

                        override fun shouldInterceptRequest(
                            view: WebView,
                            request: WebResourceRequest,
                        ): WebResourceResponse? {
                            val url = request.url
                            if (url.toString().contains("/props/api/collections")) {
                                println("XYI 2" + request.url)
                                runOnUiThread {
                                    evaluateJavascript(
                                        "(function() {" +
                                            "var originalFetch = window.fetch;" +
                                            "window.fetch = function(input, init) {" +
                                            "return originalFetch.apply(this, arguments).then(function(response) {" +
                                            "if (response) {" +
                                            // You can customize this part to handle different response types
                                            "response.json().then(function(json) {" +
                                            "Android.onJsonResponse(JSON.stringify(json));" +
                                            "});" +
                                            "}" +
                                            "return response;" +
                                            "});" +
                                            "};" +
                                            "})();",
                                        null
                                    );
                                }
                                return super.shouldInterceptRequest(view, request)
                            }

                            return super.shouldInterceptRequest(view, request)
                        }
                    }
                    settings.javaScriptEnabled = true

                    loadUrl(mUrl)
                }
            })

            // App(root)
        }
    }
}

class JavaScriptInterface {

    @JavascriptInterface
    fun onJsonResponse(jsonResponse: String) {
        println("TEST")
        // Handle the JSON response here
        println("WebView" + "JSON Response: $jsonResponse")
    }
}
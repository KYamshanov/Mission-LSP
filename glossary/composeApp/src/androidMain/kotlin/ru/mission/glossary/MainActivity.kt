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
import org.koin.core.context.GlobalContext
import ru.mission.glossary.components.factory.RootComponentFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = GlobalContext.get().get<RootComponentFactory>().create(
            componentContext = defaultComponentContext(),
        )

        setContent {

            App(root)

            val mUrl =
                "https://translate.yandex.ru/subscribe?collection_id=6549257082cf737777c1706b&utm_source=new_collection_share_desktop"

            AndroidView(
                modifier = Modifier.alpha(0f),
                factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )


                    addJavascriptInterface(JavaScriptInterface(this@MainActivity, this), "Android")

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
                            if(url.contains("/props/api/collections")){
                                view.loadUrl("javascript:window.Android.processContent(document.getElementsByTagName('body')[0].innerText);");
                            }
                            super.onPageFinished(view, url)
                        }
                    }

                    @SuppressLint("SetJavaScriptEnabled")
                    settings.javaScriptEnabled = true

                    loadUrl(mUrl)
                }
            })

            // App(root)
        }
    }
}

class JavaScriptInterface(private val activity: MainActivity, private val webView: WebView) {

    @JavascriptInterface
    fun processContent(aContent: String) {
        println("TEXT:: $aContent")
    }
}
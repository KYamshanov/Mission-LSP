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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import ru.mission.glossary.components.factory.RootComponentFactory

class MainActivity : ComponentActivity() {

    private val androidSpaViewModel = GlobalContext.get().get<AndroidSpaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = GlobalContext.get().get<RootComponentFactory>().create(
            componentContext = defaultComponentContext(),
        )

        setContent {

            App(root)

            val isWebViewActive by androidSpaViewModel.webViewState.map {
                it !is AndroidSpaViewModel.Model.Inactive
            }.collectAsState(false)

            if (isWebViewActive) {
                val scope = rememberCoroutineScope()
                AndroidView(
                    modifier = Modifier.alpha(0f),
                    factory = {
                        WebView(it).apply {
                            scope.launch {
                                androidSpaViewModel.webViewState.collect { lSpaModel ->
                                    if (lSpaModel is AndroidSpaViewModel.Model.LoadUrl) {
                                        loadUrl(lSpaModel.url)
                                    } else if (lSpaModel is AndroidSpaViewModel.Model.FetchUrl) {
                                        loadUrl(lSpaModel.url)
                                    }
                                }
                            }


                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            addJavascriptInterface(
                                JavaScriptInterface { body ->
                                    val lSpaModel = androidSpaViewModel.webViewState.value
                                    if (lSpaModel is AndroidSpaViewModel.Model.FetchUrl) {
                                        androidSpaViewModel.onResponseReceived(lSpaModel.url, body)
                                    }
                                },
                                "Android"
                            )


                            webViewClient = object : WebViewClient() {

                                var v = true

                                override fun shouldInterceptRequest(
                                    view: WebView,
                                    request: WebResourceRequest,
                                ): WebResourceResponse? {
                                    val url = request.url.toString()
                                    if (androidSpaViewModel.interceptRequest(url)) {
                                        return null
                                    }
                                    return super.shouldInterceptRequest(view, request)
                                }

                                override fun onPageFinished(view: WebView, url: String) {
                                    val lSpaModel = androidSpaViewModel.webViewState.value
                                    if (lSpaModel is AndroidSpaViewModel.Model.FetchUrl && lSpaModel.url == url) {
                                        view.loadUrl("javascript:window.Android.processContent(document.getElementsByTagName('body')[0].innerText);")
                                    }
                                    super.onPageFinished(view, url)
                                }
                            }

                            @SuppressLint("SetJavaScriptEnabled")
                            settings.javaScriptEnabled = true

                        }
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

class JavaScriptInterface(
    val onProcessContent: (body: String) -> Unit,
) {

    @JavascriptInterface
    fun processContent(aContent: String) {
        onProcessContent(aContent)
    }
}
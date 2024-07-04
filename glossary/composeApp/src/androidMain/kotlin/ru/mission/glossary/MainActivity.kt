package ru.mission.glossary

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.defaultComponentContext
import org.koin.core.context.GlobalContext
import ru.mission.glossary.components.factory.RootComponentFactory
import java.net.URL


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = GlobalContext.get().get<RootComponentFactory>().create(
            componentContext = defaultComponentContext(),
        )

        setContent {


            val mUrl = "https://translate.yandex.ru/subscribe?collection_id=6549257082cf737777c1706b&utm_source=new_collection_share_desktop"

            println("XYI:TETS")

            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient(){

                        override fun shouldInterceptRequest(
                            view: WebView,
                            request: WebResourceRequest
                        ): WebResourceResponse? {
                            val url = request.url
                            if(url.toString().contains("/props/api/collections")){
                                println("XYI 2" + request.url)
                               // WebResourceResponse()
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
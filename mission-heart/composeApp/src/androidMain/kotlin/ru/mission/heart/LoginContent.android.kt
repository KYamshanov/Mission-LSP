package ru.mission.heart


import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.runBlocking
import ru.mission.heart.theme.MissionTheme
import ru.mission.heart.component.LoginComponent

@Composable
actual fun LoginContent(component: LoginComponent, modifier: Modifier) {
    Box(modifier = Modifier.fillMaxSize().background(color = MissionTheme.colors.background)) {
        val contentState by component.model.subscribeAsState()
        
     /*   val codeVerifier = loginScreenRootComponent.authenticationInteractor.getCodeVerifier()
        val requestState = loginScreenRootComponent.authenticationInteractor.getCodeVerifier()
*/
        val mUrl = contentState.authorizationUrl

        // Adding a WebView inside AndroidView
        // with layout as full screen
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {

                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            url: String?,
                        ): Boolean {
                            if (url != null && url.startsWith("http://127.0.0.1:8080/desktop/authorized?code=")) {
                                runBlocking {
                                    component.onAuthorized(url)
                                }
                                return true
                            }
                            return false
                        }
                    }.apply { settings.javaScriptEnabled = true }
                    loadUrl(mUrl)
                }
            }, update = {
                it.loadUrl(mUrl)
            })
    }
}
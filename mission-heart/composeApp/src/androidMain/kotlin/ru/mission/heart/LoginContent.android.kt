package ru.mission.heart


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.runBlocking
import ru.mission.heart.theme.MissionTheme
import ru.mission.heart.component.LoginComponent

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun LoginContent(component: LoginComponent, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MissionTheme.colors.background)
    ) {
        val contentState by component.model.subscribeAsState()
        val mUrl = contentState.authorizationUrl

    /*    val activity = LocalContext.current as Activity

        val launchIntent = Intent(Intent.ACTION_MAIN)
        launchIntent.setComponent(ComponentName("ru.mission.time","ru.mission.time.MainActivity"));
            //activity.getPackageManager().getLaunchIntentForPackage("ru.mission.time.MainActivity");
        activity.startActivity(launchIntent);

*/
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

                        @Suppress("OVERRIDE_DEPRECATION")
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            url: String?,
                        ): Boolean {
                            if (url != null && url.contains("/desktop/authorized?code=")) {
                                component.onAuthorized(url)
                                return true // do not redirect
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
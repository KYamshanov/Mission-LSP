package ru.mission.heart

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.heart.component.LoginComponent
import java.awt.Desktop
import java.net.URI

@Composable
actual fun LoginContent(component: LoginComponent, modifier: Modifier) {
    val model by component.model.subscribeAsState()

    Text(text = "Authorization will be in Blowser")

    LaunchedEffect(model.authorizationUrl) {
        openBrowser(model.authorizationUrl)
    }
}

private fun openBrowser(url: String) {
    Desktop.getDesktop().browse(URI(url))
}
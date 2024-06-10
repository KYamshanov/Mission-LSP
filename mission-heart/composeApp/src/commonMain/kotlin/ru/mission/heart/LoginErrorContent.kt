package ru.mission.heart

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.heart.component.LoginErrorComponent

@Composable
fun LoginErrorContent(component: LoginErrorComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    Text(text = model.title)
}
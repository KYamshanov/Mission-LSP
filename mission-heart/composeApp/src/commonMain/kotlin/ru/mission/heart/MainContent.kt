package ru.mission.heart

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.heart.component.MainComponent

@Composable
fun MainContent(component: MainComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    Text(text = model.title)
}
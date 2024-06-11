package ru.mission.time

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mission.point.RootComponent
import ru.mission.point.RootContent

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        Surface {
            RootContent(component = rootComponent, modifier = Modifier.fillMaxSize())
        }
    }
}
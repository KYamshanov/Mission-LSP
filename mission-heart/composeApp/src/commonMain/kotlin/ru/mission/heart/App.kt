package ru.mission.heart

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mission.heart.component.RootComponent
import ru.mission.heart.theme.MissionTheme

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MissionTheme {
        Surface {
            RootContent(component = rootComponent, modifier = Modifier.fillMaxSize())
        }
    }
}
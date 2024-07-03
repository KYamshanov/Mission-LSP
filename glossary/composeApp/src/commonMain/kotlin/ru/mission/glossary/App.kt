package ru.mission.glossary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mission.glossary.components.RootComponent

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        Surface {
            RootContent(component = rootComponent, modifier = Modifier.fillMaxSize())
        }
    }
}
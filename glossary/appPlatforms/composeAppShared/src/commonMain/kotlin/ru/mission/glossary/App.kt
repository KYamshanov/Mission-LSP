package ru.mission.glossary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.mission.glossary.components.RootComponent
import ru.mission.glossary.theme.MissionTheme

@Composable
@Preview
fun App(rootComponent: RootComponent, modifier: Modifier = Modifier.fillMaxSize()) {
    MissionTheme {
        Surface(modifier = modifier, color = MissionTheme.colors.background) {
            RootContent(component = rootComponent)
        }
    }
}
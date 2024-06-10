package ru.mission.heart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mission.heart.component.RootComponent
import ru.mission.heart.component.RootComponent.Child.*

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is MainChild -> MainContent(component = child.component)
            is LoginErrorChild -> LoginErrorContent(component = child.component)
            is SplashChild -> SplashContent(component = child.component)
            is LoginChild -> LoginContent(component = child.component)
        }
    }
}
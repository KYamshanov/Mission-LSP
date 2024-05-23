package ru.mission.point

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mission.point.RootComponent.Child.DetailsChild
import ru.mission.point.RootComponent.Child.ListChild

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is ListChild -> ListContent(component = child.component)
            is DetailsChild -> DetailsContent(component = child.component)
        }
    }
}
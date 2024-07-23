package ru.mission.glossary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mission.glossary.components.RootComponent
import ru.mission.glossary.components.RootComponent.Child.DetailsChild
import ru.mission.glossary.components.RootComponent.Child.ListChild

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
            is RootComponent.Child.LoadDictionaryChild -> LoadDictionaryContent(component = child.component)
            is RootComponent.Child.CollectionsChild -> CollectionsContent(component = child.component)
        }
    }
}
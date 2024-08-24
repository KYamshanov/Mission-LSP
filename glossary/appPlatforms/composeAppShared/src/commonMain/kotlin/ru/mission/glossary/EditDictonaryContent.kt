package ru.mission.glossary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mission.glossary.components.EditDictionaryComponent

@Composable
fun EditDictionaryContent(component: EditDictionaryComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is EditDictionaryComponent.Child.AddWordChild -> AddWordContent(component = child.component)
            is EditDictionaryComponent.Child.ViewWordsChild -> ViewWordsContent(component = child.component)
            is EditDictionaryComponent.Child.EditWordChild -> EditWordContent(component = child.component)
        }
    }
}
package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.DetailsComponent

@Composable
fun DetailsContent(component: DetailsComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    Text(text = model.title, modifier = Modifier.clickable {
        component.finish()
    })
}
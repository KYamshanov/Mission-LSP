package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    Surface(
        topContent = {
            TopBar(
                title = "Загрузка...",
                navigationListener = {
                    component.onBack()
                }
            )
        }
    ) {
        Column {
            model.items.forEach { item ->
                Text(
                    text = item,
                    modifier = Modifier.clickable { component.onItemClicked(item = item) },
                )
            }
        }
    }
}
package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import ru.mission.glossary.uikit.components.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.CollectionsComponent
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun CollectionsContent(component: CollectionsComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    Surface(
        topContent = {
            TopBar(
                title = "Создание задачи",
                navigationListener = {

                }
            )
        }
    ) {
        when (val m = model) {
            is CollectionsComponent.Done -> {
                Column {
                    for ((index, collection) in m.collections.withIndex()) {
                        Text(collection.name, modifier = Modifier.clickable { component.clickOnCollection(collection) })
                    }
                    Button(onClick = { component.loadNewCollection() }) {
                        Text("Новый")
                    }
                }
            }

            CollectionsComponent.Loading -> {
                Text("Загрузка...")
            }
        }
    }
}
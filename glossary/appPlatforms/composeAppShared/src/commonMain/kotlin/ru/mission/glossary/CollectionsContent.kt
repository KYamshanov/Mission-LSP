package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
            is CollectionsComponent.Model.Done -> {
                Column {
                    for ((index, collection) in m.collections.withIndex()) {
                        Row {
                            Text(
                                collection.name,
                                modifier = Modifier.clickable { component.clickOnCollection(collection) })
                            Button(onClick = {
                                component.shareCollection(collection)
                            }) {
                                Text("Share")
                            }

                            Button(onClick = {
                                component.editCollection(collection)
                            }) {
                                Text("Edit")
                            }
                        }
                    }
                    Button(onClick = { component.loadNewCollection() }) {
                        Text("Новый")
                    }
                }
            }

            CollectionsComponent.Model.Loading -> {
                Text("Загрузка...")
            }
        }
    }
}
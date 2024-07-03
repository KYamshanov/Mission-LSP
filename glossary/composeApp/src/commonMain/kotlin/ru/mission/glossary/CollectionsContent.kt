package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.CollectionsComponent

@Composable
fun CollectionsContent(component: CollectionsComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    when (val m = model) {
        is CollectionsComponent.Done -> {
            LazyColumn {
                for ((index, collection) in m.collections.withIndex()) {
                    item {
                        Text(collection.name, modifier = Modifier.clickable { component.clickOnCollection(collection) })
                    }
                }
                item {
                    Button(onClick = { component.loadNewCollection() }) {
                        Text("Новый")
                    }
                }
            }
        }

        CollectionsComponent.Loading -> {
            Text("Загрузка...")
        }
    }

}
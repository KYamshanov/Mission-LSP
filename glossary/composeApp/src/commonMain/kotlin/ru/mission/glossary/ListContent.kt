package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.ListComponent

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()

    LazyColumn {
        items(items = model.items) { item ->
            Text(
                text = item,
                modifier = Modifier.clickable { component.onItemClicked(item = item) },
            )
        }
    }
}
package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.CollectionsComponent
import ru.mission.glossary.components.DetailsComponent
import ru.mission.glossary.components.LoadDictionaryComponent
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun LoadDictionaryContent(component: LoadDictionaryComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    Surface(
        topContent = {
            TopBar(
                title = "Загрузить коллекцию",
                navigationListener = {
                    component.onBack()
                }
            )
        }
    ) {
        Column {
            TextField(
                value = model.url,
                onValueChange = { component.onSetUrl(it) }
            )

            Button(onClick = { component.onClickLoadDictionary() }) {
                Text("Load Dictionary")
            }
        }
    }
}
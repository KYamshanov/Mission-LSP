package ru.mission.glossary

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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

        Column {
            TextField(
                value = model.filePath,
                onValueChange = { component.onSetFilePath(it) }
            )

            Button(onClick = { component.onClickLoadDictionaryFromFile() }) {
                Text("Load Dictionary from file")
            }
        }
    }
}
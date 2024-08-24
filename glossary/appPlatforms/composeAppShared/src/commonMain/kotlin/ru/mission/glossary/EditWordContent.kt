package ru.mission.glossary

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.EditWordComponent
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun EditWordContent(component: EditWordComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    Surface(
        topContent = {
            TopBar(
                title = "Edit word",
                navigationListener = {
                    component.exit()
                }
            )
        }
    ) {
        TextField(value = model.word, onValueChange = { component.setWord(it) }, label = { Text("Word") })
        TextField(
            value = model.translate,
            onValueChange = { component.setTranslate(it) },
            label = { Text("Translate") })

        TextField(
            value = model.imageUrl.orEmpty(),
            onValueChange = { component.setImageUrl(it) },
            label = { Text("imageUrl") })

        TextField(
            value = model.contextSentence.orEmpty(),
            onValueChange = { component.setContextSentence(it) },
            label = { Text("contextSentence") })

        Button(onClick = { component.save() }) {
            Text("Save")
        }

        Button(onClick = { component.delete() }) {
            Text("Delete")
        }
    }
}
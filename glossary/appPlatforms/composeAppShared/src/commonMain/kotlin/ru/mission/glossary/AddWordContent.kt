package ru.mission.glossary

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.AddWordComponent
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun AddWordContent(component: AddWordComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    Surface(
        topContent = {
            TopBar(
                title = "Add word",
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
        Button(onClick = { component.save() }) {
            Text("Save")
        }
    }
}
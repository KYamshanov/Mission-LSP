package ru.mission.glossary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.EditDictionaryComponent
import ru.mission.glossary.components.ViewWordsComponent
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar

@Composable
fun ViewWordsContent(component: ViewWordsComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
    Surface(
        topContent = {
            TopBar(
                title = model.dictionary?.name.orEmpty(),
                navigationListener = {
                    component.back()
                }
            )
        }
    ) {

        Button(onClick = { component.addWord() }) {
            Text(text = "Add word")
        }

        model.dictionary?.words?.forEach { wordTranslate ->
            Text(
                "${wordTranslate.word} - ${wordTranslate.translate} ",
                modifier = Modifier.clickable { component.editWord(wordTranslate.word) })
        }
    }
}
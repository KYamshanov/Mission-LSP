package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.LoadSharedCollection
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.AddWordComponent
import ru.mission.glossary.components.EditDictionaryComponent
import ru.mission.glossary.components.LoadDictionaryComponent
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordTranslateNoId
import ru.mission.glossary.models.WordsDictionary
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class AddWordComponentImpl(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val back: () -> Unit,
) : AddWordComponent, ComponentContext by componentContext {

    private val _model = MutableValue(AddWordComponent.Model("", "", null, null))
    override val model: Value<AddWordComponent.Model> = _model

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun setWord(word: String) {
        _model.update { it.copy(word = word) }
    }

    override fun setTranslate(translate: String) {
        _model.update { it.copy(translate = translate) }
    }

    override fun save() {
        scope.launch {
            val wordModel = _model.value
            withContext(defaultContext){
                dictionary.saveWord(
                    collectionId,
                    WordTranslateNoId(wordModel.word, wordModel.translate, wordModel.imageUrl, wordModel.contextSentence)
                )
            }
            back()
        }
    }

}
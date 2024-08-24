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
import ru.mission.glossary.components.EditWordComponent
import ru.mission.glossary.models.WordTranslateWithId
import kotlin.coroutines.CoroutineContext

internal class EditWordComponentImpl(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val wordId: Long,
    private val dictionary: Dictionary,
    private val back: () -> Unit,
    private val word: String,
    private val translate: String,
    private val imageUrl: String?,
    private val contextSentence: String?,
) : EditWordComponent, ComponentContext by componentContext {

    private val _model = MutableValue(EditWordComponent.Model(word, translate, imageUrl, contextSentence))
    override val model: Value<EditWordComponent.Model> = _model

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun setWord(word: String) {
        _model.update { it.copy(word = word) }
    }

    override fun setTranslate(translate: String) {
        _model.update { it.copy(translate = translate) }
    }

    override fun setImageUrl(imageUrl: String) {
        _model.update { model1 -> model1.copy(imageUrl = imageUrl.takeIf { it.isNotBlank() }) }
    }

    override fun setContextSentence(contextSentence: String) {
        _model.update { model1 -> model1.copy(contextSentence = contextSentence.takeIf { it.isNotBlank() }) }
    }

    override fun save() {
        scope.launch {
            val wordModel = _model.value
            withContext(defaultContext) {
                dictionary.updateWord(
                    WordTranslateWithId(
                        wordId = wordId,
                        word = wordModel.word,
                        translate = wordModel.translate,
                        imageUrl = wordModel.imageUrl,
                        contextSentence = wordModel.contextSentence
                    )
                )
            }
            exit()
        }
    }

    override fun exit() {
        back()
    }

    override fun delete() {
        scope.launch {
            dictionary.removeWord(
                WordTranslateWithId(
                    wordId = wordId,
                    imageUrl = imageUrl,
                    word = word,
                    translate = translate,
                    contextSentence = null,
                )
            )
            back()
        }
    }

}
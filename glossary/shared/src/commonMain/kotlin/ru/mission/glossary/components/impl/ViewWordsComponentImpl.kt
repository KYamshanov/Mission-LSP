package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.ViewWordsComponent
import ru.mission.glossary.components.ViewWordsComponent.Model
import ru.mission.glossary.models.WordTranslateWithId
import ru.mission.glossary.models.WordsDictionary
import kotlin.coroutines.CoroutineContext

internal class ViewWordsComponentImpl(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    defaultContext: CoroutineContext,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val navToAddWordScreen: () -> Unit,
    private val exit: () -> Unit,
    private val editWord: (WordTranslateWithId) -> Unit,
) : ViewWordsComponent, ComponentContext by componentContext {

    private val _model = MutableValue(Model(null))
    override val model: Value<Model> = _model

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private var words: List<WordTranslateWithId> = emptyList()

    init {
        lifecycle.doOnStart {
            scope.launch {
                val collection = withContext(defaultContext) {
                    dictionary.getCollectionById(collectionId)
                }
                val words = withContext(defaultContext) {
                    dictionary.getWords(collection.id)
                }.also { this@ViewWordsComponentImpl.words = it }
                _model.update {
                    Model(
                        WordsDictionary(
                            name = collection.name,
                            words = words
                        )
                    )
                }
            }
        }
    }

    override fun addWord() {
        navToAddWordScreen()
    }

    override fun back() {
        exit()
    }

    override fun editWord(word: String) {
        words.firstOrNull { it.word == word }?.let { editWord(it) }
    }

}
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
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.LoadDictionaryComponent
import ru.mission.glossary.models.DictionaryGetResult
import kotlin.coroutines.CoroutineContext

internal class LoadDictionaryComponentImpl(
    componentContext: ComponentContext,
    initialUrl: String,
    mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val singleAppParser: SingleAppParser,
    private val onLoadDictionary: (Long) -> Unit,
    private val back: () -> Unit,
    private val dictionary: Dictionary,
) : LoadDictionaryComponent, ComponentContext by componentContext {

    private val _model = MutableValue(LoadDictionaryComponent.Model(initialUrl))
    override val model: Value<LoadDictionaryComponent.Model> = _model

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onSetUrl(url: String) {
        _model.update { it.copy(url = url) }
    }

    override fun onClickLoadDictionary() {
        scope.launch {
            val url = _model.value.url
            val result = withContext(defaultContext) {
                singleAppParser.parse(url)
            }
            when (result) {
                is DictionaryGetResult.Failure -> {
                    //TODO()
                }

                is DictionaryGetResult.Success -> {
                    val dictionary = result.dictionary
                    val collection =
                        this@LoadDictionaryComponentImpl.dictionary.saveCollection(dictionary.name, dictionary.words)
                    onLoadDictionary(collection.id)
                }
            }
        }
    }

    override fun onBack() {
        back()
    }
}
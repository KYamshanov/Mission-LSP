package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.*
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.models.DictionaryGetResult
import kotlin.coroutines.CoroutineContext

class DefaultListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    defaultContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val singleAppParser: SingleAppParser,
    private val url: String,
) : ListComponent, ComponentContext by componentContext {

    private val _model = MutableValue(ListComponent.Model(listOf("Hello wolrd!", "Hi!")))

    override val model: Value<ListComponent.Model> = _model

    override fun onItemClicked(item: String) = onItemSelected(item)

    val scope = coroutineScope(mainContext + SupervisorJob())

    init {
        scope.launch {
            val result = withContext(defaultContext) {
                singleAppParser.parse(url)
            }
            when (result) {
                is DictionaryGetResult.Failure -> TODO()
                is DictionaryGetResult.Success -> {
                    val dictionary = result.dictionary
                    _model.update {
                        ListComponent.Model(dictionary.words.map { "${it.word} - ${it.translate}" })
                    }
                }
            }
        }
    }
}
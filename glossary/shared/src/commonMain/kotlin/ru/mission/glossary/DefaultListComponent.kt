package ru.mission.glossary

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.*
import ru.mission.glossary.models.DictionaryGetResult
import kotlin.coroutines.CoroutineContext

class DefaultListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    defaultContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val singleAppParser: SingleAppParser,
) : ListComponent, ComponentContext by componentContext {

    private val _model = MutableValue(ListComponent.Model(listOf("Hello wolrd!", "Hi!")))

    override val model: Value<ListComponent.Model> = _model

    override fun onItemClicked(item: String) = onItemSelected(item)

    val scope = coroutineScope(mainContext + SupervisorJob())

    init {
        scope.launch {
            val result = withContext(defaultContext){
                singleAppParser.parse("https://translate.yandex.ru/subscribe?collection_id=6549257082cf737777c1706b&utm_source=collection_share_touch")
            }
            when (result) {
                is DictionaryGetResult.Failure -> TODO()
                is DictionaryGetResult.Success -> TODO()
            }
        }
        // not use GLobalScope
       /* GlobalScope.launch {
            val result =
            if (result is DictionaryGetResult.Success) {
                val dictionary = result.dictionary
                _model.update {
                    ListComponent.Model(dictionary.words.map { "${it.word} - ${it.translate}" })
                }
            }
        }*/
    }
}
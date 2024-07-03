package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.ListComponent
import kotlin.coroutines.CoroutineContext

internal class DefaultListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val collectionId: Long,
    private val dictionary: Dictionary,
) : ListComponent, ComponentContext by componentContext {

    private val _model = MutableValue(ListComponent.Model(listOf("Hello wolrd!", "Hi!")))

    override val model: Value<ListComponent.Model> = _model

    override fun onItemClicked(item: String) = onItemSelected(item)

    private val scope = coroutineScope(mainContext + SupervisorJob())

    init {
        scope.launch {
            val dictionary = dictionary.getWords(collectionId)
            _model.update {
                ListComponent.Model(dictionary.map { "${it.word} - ${it.translate}" })
            }
        }
    }
}
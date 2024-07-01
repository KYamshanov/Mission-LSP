package ru.mission.glossary

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultListComponent(
    componentContext: ComponentContext,
    private val onItemSelected: (String) -> Unit
) : ListComponent, ComponentContext by componentContext {

    private val _model = MutableValue(ListComponent.Model(listOf("Hello wolrd!", "Hi!")))

    override val model: Value<ListComponent.Model> = _model

    override fun onItemClicked(item: String) = onItemSelected(item)
}
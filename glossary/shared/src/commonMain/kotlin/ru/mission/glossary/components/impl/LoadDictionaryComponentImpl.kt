package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import ru.mission.glossary.components.LoadDictionaryComponent

class LoadDictionaryComponentImpl(
    componentContext: ComponentContext,
    initialUrl: String,
    private val onLoadDictionary: (String) -> Unit,
) : LoadDictionaryComponent, ComponentContext by componentContext {

    private val _model = MutableValue(LoadDictionaryComponent.Model(initialUrl))
    override val model: Value<LoadDictionaryComponent.Model> = _model

    override fun onSetUrl(url: String) {
        _model.update { it.copy(url = url) }
    }

    override fun onClickLoadDictionary() {
        onLoadDictionary(_model.value.url)
    }
}
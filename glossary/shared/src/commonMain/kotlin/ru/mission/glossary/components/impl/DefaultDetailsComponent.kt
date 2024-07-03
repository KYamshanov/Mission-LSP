package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.glossary.components.DetailsComponent

internal class DefaultDetailsComponent(
    componentContext: ComponentContext,
    title: String,
    private val onFinished: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val _model = MutableValue(DetailsComponent.Model(title))

    override val model: Value<DetailsComponent.Model> = _model
    override fun finish() = onFinished()
}


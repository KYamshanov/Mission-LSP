package ru.mission.heart.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.heart.component.DetailsComponent

internal class DetailsComponentImpl(
    componentContext: ComponentContext,
    title: String,
    private val onFinished: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val _model = MutableValue(DetailsComponent.Model(title))

    override val model: Value<DetailsComponent.Model> = _model
    override fun finish() = onFinished()
}


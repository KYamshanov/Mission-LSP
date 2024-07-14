package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.glossary.components.CardComponent

class CardComponentImpl(
    componentContext: ComponentContext,
    title: String,
    subtitle: String,
) : CardComponent, ComponentContext by componentContext {

    private val _model = MutableValue(CardComponent.Model(title = title, subtitle = subtitle))
    override val model: Value<CardComponent.Model> = _model
}
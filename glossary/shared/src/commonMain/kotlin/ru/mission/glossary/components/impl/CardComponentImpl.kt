package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import ru.mission.glossary.components.CardComponent
import ru.mission.glossary.models.ColorRGBA

class CardComponentImpl(
    componentContext: ComponentContext,
    title: String,
    subtitle: String,
    isDraggable: Boolean,
    color: ColorRGBA,
) : CardComponent, ComponentContext by componentContext {

    private val _model =
        MutableValue(
            CardComponent.Model(
                title = title,
                subtitle = subtitle,
                isDraggable = isDraggable,
                colorRGBA = color,
                blurredSubtitle = true
            )
        )
    override val model: Value<CardComponent.Model> = _model
    override fun clickOnSubtitle() {
        _model.update { it.copy(blurredSubtitle = !it.blurredSubtitle) }
    }
}
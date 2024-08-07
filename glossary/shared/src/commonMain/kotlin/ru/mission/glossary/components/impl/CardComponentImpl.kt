package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import ru.mission.glossary.components.CardComponent

class CardComponentImpl(
    componentContext: ComponentContext,
    id: Long,
    title: String,
    subtitle: String,
    isDraggable: Boolean,
    imageUrl: String?,
    private val onSetImageUrl: ((String) -> Unit),
) : CardComponent, ComponentContext by componentContext {

    private val _model =
        MutableValue(
            CardComponent.Model(
                id = id,
                title = title,
                subtitle = subtitle,
                isDraggable = isDraggable,
                blurredSubtitle = true,
                imageUrl = imageUrl
            )
        )
    override val model: Value<CardComponent.Model> = _model
    override fun clickOnSubtitle() {
        _model.update { it.copy(blurredSubtitle = !it.blurredSubtitle) }
    }

    override fun setImageUrl(url: String) {
        onSetImageUrl(url)
    }
}
package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value
import ru.mission.glossary.models.ColorRGBA
import ru.mission.glossary.models.RandomColor

interface CardComponent {

    val model: Value<Model>

    fun clickOnSubtitle()

    fun clickOnCard()

    fun setImageUrl(url: String)

    fun setContextSentence(sentence: String)

    data class Model(
        val id: Long,
        val title: String,
        val subtitle: String = "",
        val isDraggable: Boolean,
        val colorRGBA: ColorRGBA = RandomColor(),
        val blurredSubtitle: Boolean,
        val isVisibleImage: Boolean,
        val imageUrl: String?,
        val contextSentence: String?,
    )
}

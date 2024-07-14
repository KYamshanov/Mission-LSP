package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface CardComponent {

    val model: Value<Model>

    data class Model(
        val title: String,
        val subtitle: String = "",
    )
}

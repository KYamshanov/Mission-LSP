package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface ListComponent {
    val model: Value<Model>

    fun onItemClicked(item: String)
    fun onBack()

    data class Model(
        val items: List<String>,
    )
}
package ru.mission.heart.component

import com.arkivanov.decompose.value.Value

interface ListComponent {
    val model: Value<Model>

    fun onItemClicked(item: String)

    data class Model(
        val items: List<String>,
    )
}
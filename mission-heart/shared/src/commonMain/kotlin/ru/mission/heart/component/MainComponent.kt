package ru.mission.heart.component

import com.arkivanov.decompose.value.Value

/**
 * Component of main screen
 */
interface MainComponent {
    val model: Value<Model>

    data class Model(
        val title: String,
    )
}
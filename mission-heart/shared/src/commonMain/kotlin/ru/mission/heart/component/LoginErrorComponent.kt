package ru.mission.heart.component

import com.arkivanov.decompose.value.Value

/**
 * Component of login error screen
 */
interface LoginErrorComponent {
    val model: Value<Model>

    data class Model(
        val title: String,
    )
}
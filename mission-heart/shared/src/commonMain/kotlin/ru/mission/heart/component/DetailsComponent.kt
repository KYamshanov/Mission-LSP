package ru.mission.heart

import com.arkivanov.decompose.value.Value

interface SplashComponent {
    val model: Value<Model>

    data class Model(
        val title: String,
    )
}
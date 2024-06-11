package ru.mission.point

import com.arkivanov.decompose.value.Value

interface DetailsComponent {
    val model: Value<Model>

    fun finish()

    data class Model(
        val title: String,
    )
}
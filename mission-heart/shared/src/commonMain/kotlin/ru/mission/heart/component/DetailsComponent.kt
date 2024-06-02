package ru.mission.heart.component

import com.arkivanov.decompose.value.Value

/**
 * This is component
 */
interface DetailsComponent {
    val model: Value<Model>

    fun finish()

    data class Model(
        val title: String,
    )
}
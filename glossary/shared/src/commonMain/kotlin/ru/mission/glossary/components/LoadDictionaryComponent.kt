package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface LoadDictionaryComponent {
    val model: Value<Model>

    fun onSetUrl(url: String)

    fun onClickLoadDictionary()

    data class Model(
        val url: String,
    )
}
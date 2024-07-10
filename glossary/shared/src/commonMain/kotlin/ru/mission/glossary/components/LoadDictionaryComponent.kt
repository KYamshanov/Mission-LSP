package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface LoadDictionaryComponent {
    val model: Value<Model>

    fun onSetUrl(url: String)

    fun onClickLoadDictionary()
    fun onBack()

    data class Model(
        val url: String,
    )
}
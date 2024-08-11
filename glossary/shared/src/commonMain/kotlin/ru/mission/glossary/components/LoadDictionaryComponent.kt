package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface LoadDictionaryComponent {
    val model: Value<Model>

    fun onSetUrl(url: String)

    fun onSetFilePath(absoluteFilePath: String)

    fun onClickLoadDictionary()
    fun onBack()

    fun onClickLoadDictionaryFromFile()

    data class Model(
        val url: String,
        val filePath: String,
    )
}
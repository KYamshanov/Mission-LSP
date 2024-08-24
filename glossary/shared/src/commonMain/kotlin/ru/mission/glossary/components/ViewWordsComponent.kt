package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value
import ru.mission.glossary.models.WordsDictionary

interface ViewWordsComponent {

    val model: Value<Model>

    /**
     * clicked on add word button
     *
     * I don`t now how to name methods such as click handlers
     */
    fun addWord()

    fun back()

    fun editWord(word: String)

    data class Model(
        val dictionary: WordsDictionary?,
    )
}
package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value

interface AddWordComponent {
    val model: Value<Model>

    fun setWord(word: String)

    fun setTranslate(translate: String)

    fun save()

    /**
     * left the screen
     * or back from the screen
     */
    fun exit()

    data class Model(
        val word: String,
        val translate: String,
        val imageUrl: String?,
        val contextSentence: String?,
    )
}
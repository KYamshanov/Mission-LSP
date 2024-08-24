package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value
import ru.mission.glossary.models.WordsDictionary

interface EditWordComponent {
    val model: Value<Model>

    fun setWord(word: String)

    fun setTranslate(translate: String)

    fun setImageUrl(imageUrl: String)

    fun setContextSentence(contextSentence: String)

    fun save()

    fun exit()

    fun delete()

    data class Model(
        val word: String,
        val translate: String,
        val imageUrl: String?,
        val contextSentence: String?,
    )
}
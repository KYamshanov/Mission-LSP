package ru.mission.glossary.models

import kotlinx.serialization.Serializable

sealed interface WordTranslate {

    val word: String
    val translate: String
    val imageUrl: String?
}

@Serializable
data class WordTranslateNoId(
    override val word: String,
    override val translate: String,
    override val imageUrl: String?,
) : WordTranslate

data class WordTranslateWithId(
    val wordId: Long,
    override val word: String,
    override val translate: String,
    override val imageUrl: String?,
) : WordTranslate

inline fun <reified T : WordTranslate> WordTranslate.swipeWordAndTranslate(): T =
    when (this) {
        is WordTranslateNoId -> WordTranslateNoId(
            word = translate, translate = word, imageUrl = imageUrl
        )

        is WordTranslateWithId -> WordTranslateWithId(
            wordId = wordId, word = translate, translate = word, imageUrl = imageUrl
        )
    } as T

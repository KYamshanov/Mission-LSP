package ru.mission.glossary.models

import kotlinx.serialization.Serializable

sealed interface WordTranslate {

    val word: String
    val translate: String
    val imageUrl: String?
    val contextSentence: String?
}

@Serializable
data class WordTranslateNoId(
    override val word: String,
    override val translate: String,
    override val imageUrl: String? = null,
    override val contextSentence: String? = null,
) : WordTranslate

data class WordTranslateWithId(
    val wordId: Long,
    override val word: String,
    override val translate: String,
    override val imageUrl: String?,
    override val contextSentence: String?,
) : WordTranslate

inline fun <reified T : WordTranslate> WordTranslate.swipeWordAndTranslate(): T =
    when (this) {
        is WordTranslateNoId -> WordTranslateNoId(
            word = translate, translate = word, imageUrl = imageUrl, contextSentence = contextSentence
        )

        is WordTranslateWithId -> WordTranslateWithId(
            wordId = wordId, word = translate, translate = word, imageUrl = imageUrl, contextSentence = contextSentence
        )
    } as T

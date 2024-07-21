package ru.mission.glossary.models

sealed interface WordTranslate {

    val word: String
    val translate: String
}

data class WordTranslateNoId(
    override val word: String,
    override val translate: String,
) : WordTranslate

data class WordTranslateWithId(
    val wordId: Long,
    override val word: String,
    override val translate: String,
) : WordTranslate

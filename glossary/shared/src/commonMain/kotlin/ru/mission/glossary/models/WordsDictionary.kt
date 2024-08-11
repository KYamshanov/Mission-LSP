package ru.mission.glossary.models

import kotlinx.serialization.Serializable

@Serializable
data class WordsDictionary(
    val name: String,
    val words: List<WordTranslate>,
)

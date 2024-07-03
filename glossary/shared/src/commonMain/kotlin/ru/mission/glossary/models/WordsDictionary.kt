package ru.mission.glossary.models

data class WordsDictionary(
    val name: String,
    val words: List<WordTranslate>,
)

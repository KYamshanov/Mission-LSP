package ru.mission.glossary.models

internal sealed interface DictionaryGetResult {

    data class Success(val dictionary: WordsDictionary) : DictionaryGetResult

    data class Failure(val reason: Exception) : DictionaryGetResult
}
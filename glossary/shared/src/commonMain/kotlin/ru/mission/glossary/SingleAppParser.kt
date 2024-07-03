package ru.mission.glossary

import ru.mission.glossary.models.DictionaryGetResult

internal interface SingleAppParser {

    suspend fun parse(url: String): DictionaryGetResult
}
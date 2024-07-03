package ru.mission.glossary

import ru.mission.glossary.models.DictionaryGetResult

interface SingleAppParser {

    suspend fun parse(url: String): DictionaryGetResult
}
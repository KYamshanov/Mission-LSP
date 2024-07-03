package ru.mission.glossary

import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.WordTranslate

internal interface Dictionary {

    /**
     * Get names of words collections
     */
    suspend fun getCollections(): List<Collection>

    suspend fun getCollectionById(collectionId: Long): Collection

    suspend fun getWords(collectionId: Long): List<WordTranslate>

    suspend fun saveCollection(withName: String, words: List<WordTranslate>): Collection
}
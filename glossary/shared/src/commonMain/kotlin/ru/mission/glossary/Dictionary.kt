package ru.mission.glossary

import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.TestingModel
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordTranslateWithId

internal interface Dictionary {

    /**
     * Get names of words collections
     */
    suspend fun getCollections(): List<Collection>

    suspend fun getCollectionById(collectionId: Long): Collection

    suspend fun getWords(collectionId: Long): List<WordTranslateWithId>

    suspend fun saveCollection(withName: String, words: List<WordTranslate>): Collection

    suspend fun getWordsWithTesting(collectionId: Long): List<Pair<WordTranslateWithId, TestingModel?>>

    suspend fun saveTesting(testingModel: TestingModel): TestingModel

    suspend fun setImageUrl(wordId: Long, imageUrl: String): WordTranslateWithId
}
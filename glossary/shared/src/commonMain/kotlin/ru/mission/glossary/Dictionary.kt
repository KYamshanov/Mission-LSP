package ru.mission.glossary

import ru.mission.glossary.models.*
import ru.mission.glossary.models.Collection

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

    suspend fun setContextSentence(wordId: Long, contextSentence: String): WordTranslateWithId

    suspend fun removeWord(wordTranslateWithId: WordTranslateWithId)

    suspend fun saveWord(collectionId: Long, wordTranslate: WordTranslate): WordTranslateWithId

    suspend fun updateWord(word: WordTranslateWithId): WordTranslateWithId
}
package ru.mission.glossary.data

import kotlinx.coroutines.withContext
import ru.mission.glossary.Database
import ru.mission.glossary.Dictionary
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.WordTranslate
import kotlin.coroutines.CoroutineContext

internal class SqDictionary(
    private val database: Database,
    private val ioContext: CoroutineContext,
) : Dictionary {

    override suspend fun getCollections(): List<Collection> = withContext(ioContext) {
        database.dictionaryQueries.selectAllCollections().executeAsList()
            .map { Collection(id = it.id, name = it.name) }
    }

    override suspend fun getCollectionById(collectionId: Long): Collection = withContext(ioContext) {
        database.dictionaryQueries.selectCollectionById(collectionId).executeAsOne()
            .let { Collection(it.id, it.name) }
    }

    override suspend fun getWords(collectionId: Long): List<WordTranslate> = withContext(ioContext) {
        database.dictionaryQueries.selectCollectionDictonary(collectionId).executeAsList()
            .map { WordTranslate(it.word, it.translate) }
    }

    override suspend fun saveCollection(withName: String, words: List<WordTranslate>): Collection {
        val queries = database.dictionaryQueries
        return queries.transactionWithResult {
            val collectionId = queries.insertCollection(withName)
                .run { queries.lastInsertRowId().executeAsOne() }
            val wordsIds = words.map {
                queries.insertDictionary(it.word, it.translate)
                queries.lastInsertRowId().executeAsOne()
            }
            wordsIds.forEach { wordId ->
                queries.insertCollectionWords(collectionId, wordId)
            }
            Collection(collectionId, withName)
        }
    }
}
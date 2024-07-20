package ru.mission.glossary.data

import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import ru.mission.glossary.Database
import ru.mission.glossary.Dictionary
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.TestingModel
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
            .map { WordTranslate(it.wordId, it.word, it.translate) }
    }

    override suspend fun saveCollection(withName: String, words: List<WordTranslate>): Collection =
        withContext(ioContext) {
            val queries = database.dictionaryQueries
            queries.transactionWithResult {
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

    override suspend fun getWordsWithTesting(collectionId: Long): List<Pair<WordTranslate, TestingModel?>> =
        withContext(ioContext) {
            database.dictionaryQueries.selectCollectionDictonaryWithTesting(collectionId).executeAsList()
                .map {
                    val testingModel =
                        if (it.successCount != null && it.checkCount != null && it.lastCheckDate != null) {
                            TestingModel(
                                wordId = it.wordId,
                                successCount = it.successCount,
                                checkCount = it.checkCount,
                                lastCheckDate = it.lastCheckDate.let { ms -> Instant.fromEpochMilliseconds(ms) }
                            )
                        } else null

                    WordTranslate(it.wordId, it.word, it.translate) to testingModel
                }
        }

    override suspend fun saveTesting(testingModel: TestingModel): TestingModel = withContext(ioContext) {
        val old = database.dictionaryQueries.selectTesting(testingModel.wordId).executeAsOneOrNull()
        if (old == null) {
            database.dictionaryQueries.insertTesting(
                wordId = testingModel.wordId,
                checkCount = testingModel.checkCount,
                successCount = testingModel.successCount,
                lastCheckDate = testingModel.lastCheckDate.toEpochMilliseconds()
            )
            database.dictionaryQueries.selectTesting(testingModel.wordId).executeAsOne()
        } else {
            database.dictionaryQueries.updateTesting(
                checkCount = testingModel.checkCount,
                successCount = testingModel.successCount,
                lastCheckDate = testingModel.lastCheckDate.toEpochMilliseconds(),
                wordId = testingModel.wordId,
            )
            database.dictionaryQueries.selectTesting(testingModel.wordId).executeAsOne()
        }.let {
            TestingModel(
                wordId = testingModel.wordId,
                successCount = it.successCount,
                checkCount = it.checkCount,
                lastCheckDate = Instant.fromEpochMilliseconds(it.lastCheckDate)
            )
        }
    }
}
package ru.mission.glossary.data

import kotlinx.coroutines.*
import kotlinx.datetime.Instant
import ru.mission.glossary.Database
import ru.mission.glossary.Dictionary
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.TestingModel
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordTranslateWithId
import kotlin.coroutines.CoroutineContext
import com.example.sqldelight.hockey.data.Dictionary as DataDictionary

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

    override suspend fun getWords(collectionId: Long): List<WordTranslateWithId> = withContext(ioContext) {
        database.dictionaryQueries.selectCollectionDictonary(collectionId).executeAsList()
            .map { WordTranslateWithId(
                wordId = it.wordId,
                word = it.word,
                translate = it.translate,
                imageUrl = it.imageUrl,
                contextSentence = it.contextSentence
            ) }
    }

    override suspend fun saveCollection(withName: String, words: List<WordTranslate>): Collection =
        withContext(ioContext) {
            val queries = database.dictionaryQueries
            queries.transactionWithResult {
                val collectionId = queries.insertCollection(withName)
                    .run { queries.lastInsertRowId().executeAsOne() }
                val wordsIds = words.map {
                    queries.insertDictionary(it.word, it.translate, it.imageUrl)
                    queries.lastInsertRowId().executeAsOne()
                }
                wordsIds.forEach { wordId ->
                    queries.insertCollectionWords(collectionId, wordId)
                }
                Collection(collectionId, withName)
            }
        }

    override suspend fun getWordsWithTesting(collectionId: Long): List<Pair<WordTranslateWithId, TestingModel?>> =
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

                    WordTranslateWithId(
                        wordId = it.wordId,
                        word = it.word,
                        translate = it.translate,
                        imageUrl = it.imageUrl,
                        contextSentence = it.contextSentence,
                    ) to testingModel
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

    override suspend fun setImageUrl(wordId: Long, imageUrl: String): WordTranslateWithId = withContext(ioContext) {
        with(database.dictionaryQueries) {
            setWordImageUrl(
                imageUrl = imageUrl, id = wordId
            )
            selectWord(wordId).executeAsOne().toWordTranslateWithId()
        }
    }

    override suspend fun setContextSentence(wordId: Long, contextSentence: String): WordTranslateWithId =
        withContext(ioContext) {
            with(database.dictionaryQueries) {
                setWordContextSentence(
                    contextSentence = contextSentence, id = wordId
                )
                selectWord(wordId).executeAsOne().toWordTranslateWithId()
            }
        }

    override suspend fun removeWord(wordTranslateWithId: WordTranslateWithId) {
        with(database.dictionaryQueries) {
            deleteWord(wordTranslateWithId.wordId)
        }
    }

    override suspend fun saveWord(collectionId: Long, wordTranslate: WordTranslate): WordTranslateWithId =
        withContext(ioContext) {
            with(database.dictionaryQueries) {
                transactionWithResult {
                    insertDictionary(
                        word = wordTranslate.word,
                        translate = wordTranslate.translate,
                        imageUrl = wordTranslate.imageUrl
                    )
                    val newWordId = lastInsertRowId().executeAsOne()
                    insertCollectionWords(collectionId, newWordId)
                    val dictionary = selectWord(newWordId).executeAsOne()
                    WordTranslateWithId(
                        wordId = dictionary.id,
                        word = dictionary.word,
                        translate = dictionary.translate,
                        imageUrl = dictionary.imageUrl,
                        contextSentence = dictionary.contextSentence
                    )
                }
            }
        }
}

private fun DataDictionary.toWordTranslateWithId(): WordTranslateWithId =
    WordTranslateWithId(
        wordId = id, word = word, translate = translate, imageUrl = imageUrl, contextSentence = contextSentence
    )

package ru.mission.glossary

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import ru.mission.glossary.models.TestingModel
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordTranslateWithId
import kotlin.test.Test

class RandomWordKtTest {

    @Test
    fun testBedWord() {

        val words = listOf(
            WordTranslateWithId(
                1L, "a", "g"
            ) to TestingModel(
                1, 1L, 2L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
            ),
            WordTranslateWithId(
                1L, "b", "g"
            ) to TestingModel(
                1, 1L, 1L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
            )
        )

        var countA = 0
        var countB = 0
        for (v in 0..10_000) {
            val word: WordTranslate = getRandomWord(
                words,
                mode = 1
            )
            when (word.word) {
                "a" -> countA++
                else -> countB++
            }
        }


        assert(countA > countB) { "Await that countA $countA will have more chance than countB $countB" }
    }

    @Test
    fun testNoLongCheck() {

        val words = listOf(
            WordTranslateWithId(
                1L, "a", "g"
            ) to TestingModel(
                1, 1L, 2L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds() - 1000L),
            ),
            WordTranslateWithId(
                1L, "b", "g"
            ) to TestingModel(
                1, 1L, 1L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
            )
        )

        var countA = 0
        var countB = 0
        for (v in 0..10_000) {
            val word: WordTranslate = getRandomWord(
                words,
                mode = 0
            )
            when (word.word) {
                "a" -> countA++
                else -> countB++
            }
        }


        assert(countA > countB) { "Await that countA $countA will have more chance than countB $countB" }
    }
}
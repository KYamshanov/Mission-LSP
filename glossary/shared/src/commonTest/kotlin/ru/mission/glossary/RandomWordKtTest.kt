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

        val word: WordTranslate = getRandomWord(
            listOf(
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
            ),
            mode = 0
        )

        assertEquals(WordTranslateWithId(1L,"a","g"), word)
    }

    @Test
    fun testNoLongCheck() {

        val word: WordTranslate = getRandomWord(
            listOf(
                WordTranslateWithId(
                    1L, "a", "g"
                ) to TestingModel(
                    1, 1L, 2L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()- 1000L),
                ),
                WordTranslateWithId(
                    1L, "b", "g"
                ) to TestingModel(
                    1, 1L, 1L, Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
                )
            ),
            mode = 1
        )

        assertEquals(WordTranslateWithId(1L,"a","g"), word)
    }
}
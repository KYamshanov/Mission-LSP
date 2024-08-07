package ru.mission.glossary

import kotlinx.datetime.Clock
import ru.mission.glossary.models.TestingModel
import ru.mission.glossary.models.WordTranslateWithId
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun getRandomWord(
    words: List<Pair<WordTranslateWithId, TestingModel?>>,
    random: Random = Random(Clock.System.now().toEpochMilliseconds()),
    mode: Int = random.nextInt(5),
): WordTranslateWithId {
    return when (mode) {
        0 -> {
            //random word of the no long checked
            val newWords = words.sortedByDescending {
                it.second?.lastCheckDate?.toEpochMilliseconds() ?: 0L
            }
            normalizedRandom(random, newWords)
        }

        in 1..2 -> {
            // random of bad known words
            val newWords = words.sortedByDescending {
                it.second?.let { m -> m.successCount.toDouble() / m.checkCount.toDouble() } ?: 0.0
            }
            normalizedRandom(random, newWords)
        }

        else -> {
            //any word
            words[random.nextInt(words.size)]
        }
    }.first
}

private fun <T> normalizedRandom(
    random: Random,
    values: List<T>,
): T {
    val maxR = values.size.toDouble().pow(2.0)
    val r = random.nextDouble(maxR)
    return values[sqrt(r).toInt()]
}
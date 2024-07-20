package ru.mission.glossary

import kotlinx.datetime.Clock
import ru.mission.glossary.models.TestingModel
import ru.mission.glossary.models.WordTranslate
import kotlin.random.Random

fun getRandomWord(
    words: List<Pair<WordTranslate, TestingModel?>>,
    random: Random = Random(Clock.System.now().toEpochMilliseconds()),
    mode: Int = random.nextInt(3)
): WordTranslate {
    return when (mode) {
        0 -> {
            // random of bad known words
            val newWords = words.sortedBy {
                it.second?.let { m -> m.successCount.toDouble() / m.checkCount.toDouble() } ?: 0.0
            }.subList(0, words.size / 2)
            newWords[random.nextInt(newWords.size)]
        }

        1 -> {
            //random word of the no long checked
            val newWords = words.sortedBy {
                it.second?.lastCheckDate?.toEpochMilliseconds() ?: 0L
            }.subList(0, words.size / 2)
            newWords[random.nextInt(newWords.size)]
        }

        else -> {
            //any word
            words[random.nextInt(words.size)]
        }
    }.first
}
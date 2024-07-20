package ru.mission.glossary.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun firstTestingModel(
    wordId: Long,
    isSuccess: Boolean
): TestingModel =
    TestingModel(
        wordId = wordId,
        successCount = if (isSuccess) 1L else 0L,
        checkCount = 1L,
        lastCheckDate = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
    )

data class TestingModel(
    val wordId: Long,
    val successCount: Long,
    val checkCount: Long,
    val lastCheckDate: Instant
)

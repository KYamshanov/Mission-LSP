package ru.mission.glossary.models

import kotlinx.serialization.Serializable

@Serializable
data class ShareCollectionJsonModel(
    val collectionName: String,
    val words: List<WordTranslateNoId>
)
package ru.mission.glossary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateCollectionRoot(
    @SerialName("collection") val translateCollection: TranslateCollection? = TranslateCollection(),
)
package ru.mission.glossary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Records(
    @SerialName("creationTimestamp") val creationTimestamp: Double? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("lang") val lang: String? = null,
    @SerialName("modificationTimestamp") val modificationTimestamp: Double? = null,
    @SerialName("score") val score: Int? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("translation") val translation: String? = null,
)
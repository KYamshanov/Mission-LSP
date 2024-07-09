package ru.mission.glossary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * no common. Because model can be depend on platform usage WebEngine/WebView
 */

@Serializable
data class TranslateCollectionRoot(
    @SerialName("collection") val translateCollection: TranslateCollection? = TranslateCollection(),
)
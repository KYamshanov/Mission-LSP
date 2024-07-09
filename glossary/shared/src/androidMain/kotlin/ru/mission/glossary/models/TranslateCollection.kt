package ru.mission.glossary.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * no common. Because model can be depend on platform usage WebEngine/WebView
 */

@Serializable
data class TranslateCollection(

    @SerialName("attributesTimestamp") val attributesTimestamp: Double? = null,
    @SerialName("authorName") val authorName: String? = null,
    @SerialName("authorUid") val authorUid: String? = null,
    @SerialName("color") val color: String? = null,
    @SerialName("count") val count: Int? = null,
    @SerialName("creationTimestamp") val creationTimestamp: Double? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("public") val public: Boolean? = null,
    @SerialName("records") val records: List<Records> = arrayListOf(),
    @SerialName("subscribersCount") val subscribersCount: Int? = null,
    @SerialName("type") val type: Int? = null,
    @SerialName("version") val version: Int? = null,

    )
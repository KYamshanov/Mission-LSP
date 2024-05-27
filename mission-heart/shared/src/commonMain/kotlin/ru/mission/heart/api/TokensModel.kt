package ru.mission.heart.api

import kotlinx.serialization.Serializable

@Serializable
data class TokensModel(
    val accessToken: String,
    val refreshToken: String
)
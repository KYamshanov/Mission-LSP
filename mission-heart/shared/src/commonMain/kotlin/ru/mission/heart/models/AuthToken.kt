package ru.mission.heart.models

internal data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val scope: String,
    val tokenType: String,
    val expiresIn: Long,
)
package ru.mission.heart.models

internal data class JwtTokenPair(
    val accessToken: String,
    val refreshToken: String
)
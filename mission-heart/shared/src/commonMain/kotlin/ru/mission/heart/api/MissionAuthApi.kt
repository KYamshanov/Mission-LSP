package ru.mission.heart.api

import ru.mission.heart.models.AuthToken
import ru.mission.heart.models.JwtTokenPair


internal interface MissionAuthApi {


    suspend fun refresh(refreshToken: String): JwtTokenPair

    suspend fun token(authorizationCode: String, codeVerifier: String): AuthToken
}
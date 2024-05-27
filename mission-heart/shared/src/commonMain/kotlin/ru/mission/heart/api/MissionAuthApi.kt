package ru.mission.heart.api


internal interface MissionAuthApi {


    suspend fun refresh(refreshToken: String): TokensModel
}
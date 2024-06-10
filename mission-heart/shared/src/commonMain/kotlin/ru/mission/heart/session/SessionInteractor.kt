package ru.mission.heart.session

import kotlinx.coroutines.flow.StateFlow

internal interface SessionInteractor {
    val state: StateFlow<Session>

    suspend fun refresh(): Session

    @Throws(IllegalStateException::class)
    suspend fun auhtorize(
        authorizationCode: String,
        codeVerifier: String,
        exchangeState: String,
        requiredExchangeState: String,
    ): Session
}
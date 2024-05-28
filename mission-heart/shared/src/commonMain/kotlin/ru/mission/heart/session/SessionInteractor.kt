package ru.mission.heart.session

import kotlinx.coroutines.flow.StateFlow

internal interface SessionInteractor {
    val state: StateFlow<Session>

    suspend fun refresh(): Session
}
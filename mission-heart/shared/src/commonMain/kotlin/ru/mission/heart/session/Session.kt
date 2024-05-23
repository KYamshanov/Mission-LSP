package ru.mission.heart.session

sealed interface Session


interface AccessToken {

    val accessToken: String
}

interface RefreshToken {

    val refreshToken: String
}

/**
 * Not initialized
 * the session has not started to set up
 */
data object NotInited : Session

/**
 * Error session state
 * It will be established when refresh throw exception
 */
data class Failed(val error: Throwable) : Session
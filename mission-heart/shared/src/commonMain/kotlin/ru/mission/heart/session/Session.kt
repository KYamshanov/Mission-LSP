package ru.mission.heart.session

sealed interface Session


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


/**
 * interface provide access to `access token`
 */
interface AccessToken {

    val accessToken: String
}

/**
 * interface provide access to `refresh token`
 */
interface RefreshToken {

    val refreshToken: String
}

/**
 * Logged session by using OOauth2.0 specification and JWT tokens
 */
data class JwtSession(
    override val accessToken: String,
    override val refreshToken: String,
) : AccessToken, RefreshToken, Session
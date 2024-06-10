package ru.mission.heart.api

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mission.heart.api.dto.TokensRsDto
import ru.mission.heart.models.AuthToken
import ru.mission.heart.models.JwtTokenPair
import ru.mission.heart.network.RequestFactory

internal class MissionAuthApiImpl(
    private val requestFactory: RequestFactory
) : MissionAuthApi {

    override suspend fun refresh(refreshToken: String): JwtTokenPair =
        tokenRequest(refreshToken)
            .let {
                JwtTokenPair(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken
                )
            }

    override suspend fun token(authorizationCode: String, codeVerifier: String): AuthToken =
        tokenRequest(authorizationCode, codeVerifier)
            .let {
                AuthToken(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                    scope = it.scope,
                    tokenType = it.tokenType,
                    expiresIn = it.expiresIn
                )
            }

    private suspend fun tokenRequest(refreshToken: String): TokensRsDto = withContext(Dispatchers.Default) {
        val response = requestFactory.post("oauth2/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "grant_type" to "refresh_token",
                    "refresh_token" to refreshToken,
                ).formUrlEncode()
            )
            header("Authorization", "Basic ZGVza3RvcC1jbGllbnQ6c2VjcmV0")//desktop-client:secret
        }
        response.body()
    }

    private suspend fun tokenRequest(
        authorizationCode: String,
        codeVerifier: String,
    ): TokensRsDto = withContext(Dispatchers.Default) {
        val response = requestFactory.post("oauth2/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "grant_type" to "authorization_code",
                    "code" to authorizationCode,
                    "code_verifier" to codeVerifier
                ).formUrlEncode()
            )
            header("Authorization", "Basic ZGVza3RvcC1jbGllbnQ6c2VjcmV0")//desktop-client:secret
        }
        response.body()
    }
}
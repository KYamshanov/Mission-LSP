package ru.mission.heart.network


import io.github.aakira.napier.Napier
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.mission.heart.session.AccessToken
import ru.mission.heart.session.SessionInteractor

internal class RequestFactoryImpl(
    private val config: NetworkConfig,
    private val sessionInteractor: Lazy<SessionInteractor>,
) : RequestFactory {

    private val client = httpClient {
        install(Logging) {
            logger = NetworkLogger()
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 120_000
            socketTimeoutMillis = 120_000
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = getAuthorizationHeader() ?: return@loadTokens null
                    BearerTokens(accessToken, "")
                }
                refreshTokens {
                    try {
                        val session = sessionInteractor.value.refresh()
                        if (session !is AccessToken) {
                            throw IllegalStateException("Refresh session has no access token")
                        }
                        BearerTokens(session.accessToken, "")
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        null
                    }
                }
            }
        }

        defaultRequest {
            url("${config.requestUrl}/")
        }
    }

    override suspend fun get(endpoint: String, block: HttpRequestBuilder.() -> Unit) =
        client.get(endpoint, block)

    override suspend fun post(endpoint: String, block: HttpRequestBuilder.() -> Unit) =
        client.post(endpoint, block)

    override suspend fun delete(endpoint: String, block: HttpRequestBuilder.() -> Unit): HttpResponse =
        client.delete(endpoint, block)

    override suspend fun patch(endpoint: String, block: HttpRequestBuilder.() -> Unit): HttpResponse =
        client.patch(endpoint, block)

    private fun getAuthorizationHeader(): String? =
        (sessionInteractor.value.state.value as? AccessToken)?.accessToken

    private inner class NetworkLogger : Logger {

        override fun log(message: String) {
            Napier.d(message, tag = LOG_TEG)
        }
    }

    private companion object {

        const val LOG_TEG = "Network"
    }
}
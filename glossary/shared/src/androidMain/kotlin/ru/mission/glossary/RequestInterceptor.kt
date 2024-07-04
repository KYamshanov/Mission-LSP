package ru.mission.glossary

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import io.ktor.http.headers
import io.ktor.http.headersOf
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.net.URL
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking

interface RequestInterceptor {

    fun response(request: WebResourceRequest): WebResourceResponse
}

internal class RequestInterceptorImpl(
    private val httpClient: HttpClient,
) : RequestInterceptor {

    override fun response(request: WebResourceRequest): WebResourceResponse =
        runBlocking {
            val response = httpClient.post(URL(request.url.toString())) {
                headers {
                    request.requestHeaders.forEach { t, u -> header(t, u) }
                }
            }
            println("XYIr: ${response.toString()}")
            WebResourceResponse(
                response.headers.get("content-type") ?: null,
                response.headers.get("content-encoding") ?: "utf-8",
                response.bodyAsText().byteInputStream()
            )
        }
}
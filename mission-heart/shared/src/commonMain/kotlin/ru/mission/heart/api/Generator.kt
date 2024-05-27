package ru.mission.heart.api

internal interface Generator {

    fun generateCodeVerifier(): String

    fun generateCodeChallenge(codeVerifier: String): String
}
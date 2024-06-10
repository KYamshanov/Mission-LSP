package ru.mission.heart

internal interface Generator {

    fun generateCodeVerifier(): String

    fun generateCodeChallenge(codeVerifier: String): String
}
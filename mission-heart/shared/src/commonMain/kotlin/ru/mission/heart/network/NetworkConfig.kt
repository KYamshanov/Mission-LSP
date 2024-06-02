package ru.mission.heart.network

internal sealed interface NetworkConfig {
    val requestUrl: String
    val authorizationUrl: String
}

internal class LocalNetworkConfig : NetworkConfig {
    override val requestUrl: String = "https://127.0.0.1:3456"
    override val authorizationUrl: String = "https://127.0.0.1:6543"
}
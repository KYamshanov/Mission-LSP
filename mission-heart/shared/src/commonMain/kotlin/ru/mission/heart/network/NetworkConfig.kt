package ru.mission.heart.network

internal sealed interface NetworkConfig {
    val requestUrl: String
    val authorizationUrl: String
}

internal class LocalNetworkConfig : NetworkConfig {
    override val requestUrl: String = "http://192.168.43.29:3456"
    override val authorizationUrl: String = "http://192.168.43.29:6543"
}
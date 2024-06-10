package ru.mission.heart.network

internal sealed interface NetworkConfig {
    val requestUrl: String
    val authorizationUrl: String
}

internal class LocalNetworkConfig : NetworkConfig {

    private val ip = "10.2.15.47"

    override val requestUrl: String = "http://$ip:3456"
    override val authorizationUrl: String = "http://$ip:6543"
}
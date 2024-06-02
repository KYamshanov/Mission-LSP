package ru.mission.heart.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.heart.api.Generator
import ru.mission.heart.component.LoginComponent
import ru.mission.heart.network.NetworkConfig

internal class AndroidLoginComponentImpl(
    componentContext: ComponentContext,
    private val generator: Generator,
    private val networkConfig: NetworkConfig,
) : LoginComponent, ComponentContext by componentContext {

    private val codeVerifier = generator.generateCodeVerifier()
    private val requestState = generator.generateCodeVerifier()

    private val _model = MutableValue(LoginComponent.Model(authorizationUrl = getAuthorizationUrl()))
    override val model: Value<LoginComponent.Model> = _model

    private val callbackPort = 8080

    private fun getAuthorizationUrl() = buildString {
        val responseType = "code"
        val clientId = "desktop-client"
        val scope = "point"

        //redirectUri use not security channel (http) and it`s ok (quite security) due to code verifier
        val redirectUri = "http://127.0.0.1:${callbackPort}/desktop/authorized"
        val codeChallenge = generator.generateCodeChallenge(codeVerifier)

        append("${networkConfig.authorizationUrl}/oauth2/authorize?")
        append("client_id=$clientId&")
        append("response_type=$responseType&")
        append("scope=$scope&")
        append("redirect_uri=$redirectUri&")
        append("code_challenge=$codeChallenge&")
        append("code_challenge_method=S256&")
        append("state=$requestState")
    }

    override fun onAuthorized(url: String) {
        /*
         val authorizationCode =
                                        checkNotNull(authenticationInteractor.obtainAuthorizationCode(url)) { "AuthorizationCode not found at response $url" }
                                    val state =
                                        checkNotNull(authenticationInteractor.obtainState(url)) { "State not found at response $url" }

                                    if (state != requestState) throw IllegalStateException("State is not matched")

                                    val token =
                                        authenticationInteractor.getToken(authorizationCode, codeVerifier)
                                    val accessData = AccessData(
                                        Token(token.accessToken), Token(token.refreshToken),
                                        emptyList()
                                    )
                                    sessionFront.openSession(accessData)
                                    navigator.dismissAlert()
         */
        TODO("Not yet implemented")
    }

}
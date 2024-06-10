package ru.mission.heart.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mission.heart.Generator
import ru.mission.heart.api.MissionAuthApi
import ru.mission.heart.component.LoginComponent
import ru.mission.heart.network.NetworkConfig
import ru.mission.heart.session.SessionInteractor
import kotlin.coroutines.CoroutineContext

internal class AndroidLoginComponentImpl(
    componentContext: ComponentContext,
    private val generator: Generator,
    private val networkConfig: NetworkConfig,
    mainContext: CoroutineContext,
    private val sessionInteractor: SessionInteractor,
    private val onSussessSingIn: () -> Unit,
    private val onFailedSingIn: () -> Unit,
) : LoginComponent, ComponentContext by componentContext {

    //UI state model provided by MutableValue
    private val _model = MutableValue(LoginComponent.Model(authorizationUrl = getAuthorizationUrl()))
    override val model: Value<LoginComponent.Model> = _model

    //authoriation url configurations
    private val codeVerifier = generator.generateCodeVerifier()
    private val requestState = generator.generateCodeVerifier()
    private val callbackPort = 8080

    //coroutine scope
    private val scope = coroutineScope(mainContext + SupervisorJob())

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
        scope.launch(CoroutineExceptionHandler { ctx, throwable -> onFailedSingIn() }) {
            val authorizationCode =
                checkNotNull(obtainAuthorizationCode(url)) { "AuthorizationCode not found at response $url" }
            val state =
                checkNotNull(obtainState(url)) { "State not found at response $url" }
            sessionInteractor.auhtorize(
                authorizationCode = authorizationCode,
                codeVerifier = codeVerifier,
                exchangeState = state,
                requiredExchangeState = requestState
            )
            onSussessSingIn()
        }
    }

    private fun obtainAuthorizationCode(authorizedUrl: String): String? {
        return authorizedUrl.split("code=").getOrNull(1)?.split("&")?.getOrNull(0)
    }

    private fun obtainState(authorizedUrl: String): String? {
        return authorizedUrl.split("state=").getOrNull(1)?.split(" ")?.getOrNull(0)
    }

}
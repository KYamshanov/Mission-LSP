package ru.mission.heart.session

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mission.heart.api.MissionAuthApi
import ru.mission.heart.Preferences

internal class JwtSessionInteractor(
    private val refreshTokenKey: String,
    private val preferences: Preferences,
    private val missionAuthApi: MissionAuthApi,
) : SessionInteractor {

    private val _state = MutableStateFlow<Session>(NotInited)

    override val state: StateFlow<Session> = _state.asStateFlow()

    init {
        firstTryToRefresh()
    }

    /**
     * Would be invoked when application started
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun firstTryToRefresh() {
        GlobalScope.launch {
            try {
                refresh()
            } catch (e: Exception) {
                _state.update { Failed(e) }
            }
        }
    }

    override suspend fun refresh(): Session {
        val refreshToken: String = checkNotNull(preferences.getValue(refreshTokenKey)) { "Access token has not set" }
        val session =
            try {
                val tokensModel = missionAuthApi.refresh(refreshToken)
                preferences.saveValue(refreshTokenKey, tokensModel.refreshToken)
                JwtSession(
                    accessToken = tokensModel.accessToken,
                    refreshToken = tokensModel.refreshToken,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Failed(e)
            }
        _state.update { session }
        return session
    }

    override suspend fun auhtorize(
        authorizationCode: String,
        codeVerifier: String,
        exchangeState: String,
        requiredExchangeState: String
    ): Session {
        check(exchangeState == requiredExchangeState) { "State is not matched" }

        val token = missionAuthApi.token(authorizationCode, codeVerifier)
        preferences.saveValue(refreshTokenKey, token.refreshToken)
        val session = JwtSession(token.accessToken, token.refreshToken)
        _state.update { session }
        return session
    }

}
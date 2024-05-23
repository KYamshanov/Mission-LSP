package ru.mission.heart.session

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


internal class JwtSessionInteractor(
    private val accessTokenKey: String,
    private val refreshTokenKey: String,
) : SessionInteractor {

    private val _state = MutableStateFlow<Session>(NotInited)

    override val state: StateFlow<Session> = _state.asStateFlow()

    init {


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
        TODO()
    }

}
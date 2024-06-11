package ru.mission.heart.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.mission.heart.services.AuthorizationServiceAgreement.REQUEST_ACCESS_TOKEN
import ru.mission.heart.session.AccessToken
import ru.mission.heart.session.NotInited
import ru.mission.heart.session.SessionInteractor
import java.lang.IllegalStateException


class AuthorizationService : Service(), KoinComponent {

    private var scope = CoroutineScope(SupervisorJob())
    private var sessionInteractor: SessionInteractor? = null

    override fun onBind(intent: Intent): IBinder =
        Messenger(
            IncomingHandler(
                getSynchronizedToken = ::getSynchronizedToken,
                getRefreshedToken = ::getRefreshedToken,
            )
        ).binder

    private fun getSynchronizedToken(message: Message) {
        val rT = message.replyTo
        scope.launch {
            try {
                val session = sessionInteractor!!.state.filter { it !is NotInited }.first()
                if (session is AccessToken) {
                    rT.response(
                        AuthorizationServiceAgreement.GET_ACCESS_TOKEN_SUCCESS,
                        obj = Bundle().apply { putString("token", "Some token") }
                    )
                } else {
                    //  throw IllegalStateException("Not authorized")
                    rT.response(
                        AuthorizationServiceAgreement.GET_ACCESS_TOKEN_SUCCESS,
                        obj = Bundle().apply { putString("token", "Some token") }
                    )
                }
            } catch (e: Exception) {
                Log.wtf("AuthorizationService", e)
                println(message.replyTo)
                rT.response(AuthorizationServiceAgreement.GET_ACCESS_TOKEN_ERROR)
            }
        }
    }


    private fun getRefreshedToken(message: Message) {
        val rT = message.replyTo
        scope.launch {
            try {
                val session = sessionInteractor!!.refresh()
                if (session is AccessToken) {
                    rT.response(
                        AuthorizationServiceAgreement.GET_ACCESS_TOKEN_SUCCESS,
                        obj = Bundle().apply { putString("token", "Some token") }
                    )
                } else {
                    throw IllegalStateException("Not authorized")
                }
            } catch (e: Exception) {
                Log.wtf("AuthorizationService", e)
                rT.response(AuthorizationServiceAgreement.GET_ACCESS_TOKEN_ERROR)
            }
        }
    }

    /**
     * Handler of incoming messages from clients.
     */
    private class IncomingHandler(
        private val getSynchronizedToken: (Message) -> Unit,
        private val getRefreshedToken: (Message) -> Unit,
    ) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                REQUEST_ACCESS_TOKEN -> {
                    println(msg.replyTo)
                    if (msg.arg1 == 100) getRefreshedToken(msg)
                    else getSynchronizedToken(msg)
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        Log.d("AuthorizationService", "AuthorizationService: OnCreate")
        scope = CoroutineScope(SupervisorJob())
        sessionInteractor = get<SessionInteractor>()
    }

    override fun onDestroy() {
        Log.d("AuthorizationService", "AuthorizationService: OnDestroy")
        scope.cancel("Service has been stoped")
        sessionInteractor = null
    }
}

private fun Messenger.response(
    what: Int,
    arg1: Int = 0,
    arg2: Int = 0,
    obj: Parcelable? = null
) =
    send(
        Message.obtain(null, what, 0, 0).apply {
            if (obj != null) this.obj = obj
        }
    )
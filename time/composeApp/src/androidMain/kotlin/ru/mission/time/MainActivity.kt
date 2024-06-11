package ru.mission.time

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mission.point.DefaultRootComponent


class MainActivity : ComponentActivity() {

    val REQUEST_ACCESS_TOKEN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root =
            DefaultRootComponent(
                componentContext = defaultComponentContext(),
            )

        setContent {
            App(root)
        }
        
        println("Start")

        lifecycleScope.launch {
            delay(1000L)
            val msg: Message = Message.obtain(null, REQUEST_ACCESS_TOKEN, 0, 0)
            msg.replyTo = messenger
            try {
                mService?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    private var mService: Messenger? = null


    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            println("onServiceConnected")
            mService = Messenger(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            println("onServiceDisconnected")
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.
        Intent().also { intent ->
            intent.setComponent(ComponentName("ru.mission.heart","ru.mission.heart.services.AuthorizationService"))
            println(bindService(intent, connection, Context.BIND_AUTO_CREATE))
            }
    }

    val messenger = Messenger(ResponseHandler())


    private inner class ResponseHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Toast.makeText(this@MainActivity, "message from service", Toast.LENGTH_SHORT).show();
            println(msg.obj)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
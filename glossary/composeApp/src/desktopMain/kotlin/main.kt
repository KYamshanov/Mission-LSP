import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.GlobalContext
import ru.mission.glossary.App
import ru.mission.glossary.di.initKoin
import ru.mission.glossary.components.factory.RootComponentFactory

fun main() {
    initKoin { }
    val lifecycle = LifecycleRegistry()

    // Always create the root component outside Compose on the UI thread
    val root =
        runOnUiThread {
            val componentContext = DefaultComponentContext(lifecycle = lifecycle)
            GlobalContext.get().get<RootComponentFactory>().create(componentContext)
        }

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Mission Point",
        ) {
            App(root)
        }
    }
}
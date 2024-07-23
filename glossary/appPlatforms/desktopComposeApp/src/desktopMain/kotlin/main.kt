import androidx.compose.ui.awt.ComposePanel
import ca.fredperr.customtitlebar.titlebar.TBJFrame
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.GlobalContext
import ru.mission.glossary.App
import ru.mission.glossary.components.factory.RootComponentFactory
import ru.mission.glossary.di.initKoin
import java.awt.Dimension
import javax.swing.*


fun main() {
    initKoin { }
    val lifecycle = LifecycleRegistry()

    // Always create the root component outside Compose on the UI thread
    val root =
        runOnUiThread {
            val componentContext = DefaultComponentContext(lifecycle = lifecycle)
            GlobalContext.get().get<RootComponentFactory>().create(componentContext)
        }


    SwingUtilities.invokeLater {
        System.setProperty("compose.swing.render.on.graphics", "true")

        val frame = TBJFrame("Demo")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.minimumSize = Dimension(720, 480)
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true)

        val controlPanel = ComposePanel()
        frame.frameContentPane.add(controlPanel)
        controlPanel.setContent {
            App(rootComponent = root)
        }
    }
}
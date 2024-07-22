import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import lib.ca.fredperr.customtitlebar.titlebar.TBJFrame
import lib.ca.fredperr.customtitlebar.titlebar.theme.DarkTBTheme
import lib.ca.fredperr.customtitlebar.titlebar.win.WindowFrameType
import org.koin.core.context.GlobalContext
import ru.mission.glossary.App
import ru.mission.glossary.components.factory.RootComponentFactory
import ru.mission.glossary.di.initKoin
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
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
        val window: lib.ca.fredperr.customtitlebar.titlebar.TBJFrame =
            lib.ca.fredperr.customtitlebar.titlebar.TBJFrame(
                "TBDemo",
                lib.ca.fredperr.customtitlebar.titlebar.win.WindowFrameType.NORMAL,
                lib.ca.fredperr.customtitlebar.titlebar.theme.DarkTBTheme(),
                20
            )
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        window.setMinimumSize(Dimension(720, 480))
        window.setLocationRelativeTo(null)


        // Setting UI attributes
        val font = Font("SansSerif", Font.PLAIN, 14)

        UIManager.put("Menu.selectionBackground", java.awt.Color(177, 96, 96))
        UIManager.put("Menu.selectionForeground", java.awt.Color.lightGray)
        UIManager.put("MenuBar.background", window.getTheme().getFrameBackground())
        UIManager.put("Menu.background", window.getTheme().getFrameBackground())
        UIManager.put("Menu.foreground", java.awt.Color.lightGray)
        UIManager.put("Menu.border", BorderFactory.createEmptyBorder(5, 2, 5, 2))
        UIManager.put("Menu.font", font)


        // Adding some components
        val menuBar = JMenuBar()
        menuBar.border = BorderFactory.createEmptyBorder()
        menuBar.add(JMenu("File"))
        menuBar.add(JMenu("Edit"))
        menuBar.add(JMenu("View"))
        menuBar.add(JMenu("Help"))

        val title: JLabel = JLabel(window.getTitle())
        title.font = font
        title.foreground = java.awt.Color.GRAY
        title.border = BorderFactory.createEmptyBorder(0, 0, 0, 5)

        window.getCustomAreaPanel().add(title)
        window.getCustomAreaPanel().add(menuBar)

        window.pack()
        window.setVisible(true)
        // addind ComposePanel on JFrame

        val composePanel = ComposePanel()

        window.contentPane.add(composePanel, BorderLayout.CENTER)

        // setting the content

        composePanel.setContent {
            App(root)
        }

        window.setSize(800, 600)
        window.isVisible = true
    }


/*    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Glossary",
        ) {


            window.isUndecorated = true
            window.rootPane.setWindowDecorationStyle(JRootPane.FRAME);
            //window.contentPane.layout.layoutContainer(MetalRootPaneUI())
            App(root)
        }
    }*/
}
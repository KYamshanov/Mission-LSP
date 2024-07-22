import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ca.fredperr.customtitlebar.titlebar.TBJFrame
import ca.fredperr.customtitlebar.titlebar.controls.TBMinimizeButton
import ca.fredperr.customtitlebar.titlebar.controls.TBRestoreButton
import ca.fredperr.customtitlebar.titlebar.theme.DarkTBTheme
import ca.fredperr.customtitlebar.titlebar.win.CustomDecorationWindowProc
import ca.fredperr.customtitlebar.titlebar.win.WindowFrameType
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import org.koin.core.context.GlobalContext
import ru.mission.glossary.App
import ru.mission.glossary.components.factory.RootComponentFactory
import ru.mission.glossary.di.initKoin
import java.awt.Window
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() {
    initKoin { }
    val lifecycle = LifecycleRegistry()

    // Always create the root component outside Compose on the UI thread
    val root =
        runOnUiThread {
            val componentContext = DefaultComponentContext(lifecycle = lifecycle)
            GlobalContext.get().get<RootComponentFactory>().create(componentContext)
        }

    /*

        SwingUtilities.invokeLater {
            */
    /* val window: TBJFrame =
                 TBJFrame(
                     "TBDemo",
                     WindowFrameType.NORMAL,
                     DarkTBTheme(),
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
                // App(root)
             }

             window.setSize(800, 600)
             window.isVisible = true*//*


        val frame = TBJFrame("Demo", WindowFrameType.NORMAL, DarkTBTheme(), 0)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        val composePanel = ComposePanel()
        composePanel.setContent {
            Column {
                App(root)
            }
        }
        frame.frameContentPane.add(composePanel, 0)
    }
*/


    SwingUtilities.invokeLater {



        val frame = TBJFrame("Demo", WindowFrameType.NORMAL, DarkTBTheme(), 0)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        val composePanel = ComposePanel()
        composePanel.setContent {
           /* val windowScope = object : WindowScope{
                override val window: Window
                    get() = TODO("Not yet implemented")

            }*/
            Column {
                Spacer(modifier = Modifier.height(32.dp))
                /* WindowDraggableArea {
                     Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray))
                 }*/
                //window.contentPane.layout.layoutContainer(MetalRootPaneUI())
                App(root)
            }
        }
      //  frame.frameContentPane.add(composePanel, 0)
    }
    /* val windowProcEx = CustomDecorationWindowProc()

     val frame = TBJFrame("Demo", WindowFrameType.NORMAL, DarkTBTheme(), 0)
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(720, 480);
     frame.setLocationRelativeTo(null);
     frame.pack();
     frame.setVisible(true);


     application {
         val windowState = rememberWindowState()

         LifecycleController(lifecycle, windowState)

         Window(
           //  modifier = Modifier.height(40.dp),
             onCloseRequest = ::exitApplication,
             state = windowState,
             title = "Glossary",
             //   undecorated = true
         ) {


             // CustomDecorationParameters.setTitleBarHeight(50)
             windowProcEx.init(window.getHwnd())

 *//*
            Column {
                *//**//* WindowDraggableArea {
                     Box(Modifier.fillMaxWidth().height(48.dp).background(Color.DarkGray))
                 }*//**//*
                //window.contentPane.layout.layoutContainer(MetalRootPaneUI())
                App(root)
            }*//*
        }
    }*/
}

private fun Window.getHwnd(): WinDef.HWND {
    val hwnd = WinDef.HWND()
    hwnd.pointer = Native.getComponentPointer(this)
    return hwnd
}
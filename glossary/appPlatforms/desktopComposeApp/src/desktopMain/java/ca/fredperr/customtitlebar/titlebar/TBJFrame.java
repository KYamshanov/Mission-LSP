package ca.fredperr.customtitlebar.titlebar;

import ca.fredperr.customtitlebar.titlebar.win.CustomDecorationParameters;
import ca.fredperr.customtitlebar.titlebar.win.CustomDecorationWindowProc;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TBJFrame extends JFrame {

    final CustomDecorationWindowProc windowProcEx;
    public JPanel titleBarPane, frameContentPane, controlContainer;

    public TBJFrame(String title) {
        super(title);
        this.windowProcEx = new CustomDecorationWindowProc();
        setLayout(new BorderLayout());

        frameContentPane = new JPanel();
        frameContentPane.setLayout(new BorderLayout());
        frameContentPane.setOpaque(false);

        controlContainer = new JPanel();
        controlContainer.setLayout(new GridLayout(1, 3, -1, 0));
        controlContainer.setMinimumSize(new Dimension(100, 100));

        titleBarPane = new JPanel();
        frameContentPane.setBackground(Color.CYAN);

        setContentPane(frameContentPane);
        pack();
        CustomDecorationParameters.setControlBoxWidth(controlContainer.getWidth());
    }


    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        windowProcEx.init(getHwnd());
    }

    @Override
    public void pack() {
        super.pack();
    }

    private WinDef.HWND getHwnd() {
        WinDef.HWND hwnd = new WinDef.HWND();
        hwnd.setPointer(Native.getComponentPointer(this));
        return hwnd;
    }
}

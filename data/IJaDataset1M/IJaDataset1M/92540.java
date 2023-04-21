package br.com.jteam.jfcm.gui.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * 
 * @author Rafael
 *
 */
public class GUIUtil {

    private GUIUtil() {
    }

    public static void centerWindow(Window w) {
        Dimension ownerSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (ownerSize.width - w.getSize().width) / 2;
        int y = (ownerSize.height - w.getSize().height) / 2;
        w.setLocation(x, y);
    }
}

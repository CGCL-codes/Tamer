package tippmeister.gui;

import javax.swing.*;
import tippmeister.utilities.ImageCanvas;
import java.awt.*;

/**
 * Shows the splashscreen for a defined duration
 * 
 * @author Franz
 */
@SuppressWarnings("serial")
public class FrmSplashscreen extends JWindow {

    /**
	 * Creates a new splashscreen which uses "splashscreen.jpg" as image.
	 */
    public FrmSplashscreen() {
        this.add(new ImageCanvas("splashscreen.jpg"));
        this.pack();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    /**
	 * Shows the splashscreen
	 * 
	 * @param duration Duration in seconds, how long the splashscreen is shown
	 */
    public void show(int duration) {
        this.setVisible(true);
        synchronized (this) {
            try {
                this.wait(duration * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setVisible(false);
        this.dispose();
    }
}

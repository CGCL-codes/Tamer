package cc.rober.lfslcd;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

class ExitActionListener implements ActionListener {

    private static Logger log = Logger.getLogger("cc.rober.lfslcd");

    private Thread ogThread;

    private Thread cfa635Thread;

    public ExitActionListener(Thread ogThread, Thread cfa635Thread) {
        this.ogThread = ogThread;
        this.cfa635Thread = cfa635Thread;
    }

    public void actionPerformed(ActionEvent e) {
        ogThread.interrupt();
        cfa635Thread.interrupt();
        try {
            ogThread.join(2000);
            cfa635Thread.join(2000);
        } catch (Exception ex) {
            log.debug("The join on the OutGaugeReader or CFA635Writer threads expired", ex);
        }
        log.info("Application stopped");
        System.exit(0);
    }
}

public class LFSLCD {

    private static Logger log = Logger.getLogger("cc.rober.lfslcd");

    private Configuration cfg;

    private Thread ogThread;

    private Thread cfa635Thread;

    private OutGaugeReader ogReader;

    private CFA635Writer cfa635Writer;

    private CFA635 cfa635;

    public LFSLCD() {
    }

    /**
	 * Connect to the CFA635.
	 */
    private void openCFA635() {
        cfa635 = new CFA635();
        try {
            cfa635.open(cfg.getSerialPort());
        } catch (Exception e) {
            log.fatal("Couldn't connect to the CFA635", e);
            System.exit(1);
        }
        log.info("Connected to the CFA635");
    }

    /**
	 * Initialize the CFA635: clear screen, set contrast, backlight and turn off LED's.
	 */
    private void initializeCFA635() {
        try {
            cfa635.clear();
            cfa635.setContrast(cfg.getContrast());
            cfa635.setBacklight(cfg.getBacklight());
            cfa635.setLED(0, 0.0f, 0.0f);
            cfa635.setLED(1, 0.0f, 0.0f);
            cfa635.setLED(2, 0.0f, 0.0f);
            cfa635.setLED(3, 0.0f, 0.0f);
        } catch (Exception e) {
            log.error("Couldn't initialize the CFA635", e);
        }
    }

    /**
	 *  Add's the application icon with it's menu to the system tray.
	 */
    private void createTrayIcon() {
        SystemTray sysTray = SystemTray.getSystemTray();
        Image iconImage = Toolkit.getDefaultToolkit().getImage("icon.png");
        ExitActionListener exitListener = new ExitActionListener(ogThread, cfa635Thread);
        PopupMenu popupMenu = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(exitListener);
        popupMenu.add(exitItem);
        TrayIcon trayIcon = new TrayIcon(iconImage, "LFSLCD", popupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            sysTray.add(trayIcon);
        } catch (AWTException e) {
            log.fatal("Can't add the tray icon", e);
            System.exit(1);
        }
    }

    /**
	 * Initializes the environment: loads the configuration and creates some objects.
	 */
    private void initialize() {
        try {
            cfg = new Configuration();
        } catch (Exception e) {
            log.fatal("Couldn't load the configuration", e);
            System.exit(1);
        }
        openCFA635();
        initializeCFA635();
        try {
            LinkedBlockingQueue<OutGaugePacket> dataQueue = new LinkedBlockingQueue<OutGaugePacket>();
            ogReader = new OutGaugeReader(cfg, dataQueue);
            ogThread = new Thread(ogReader, "OutGaugeReader");
            cfa635Writer = new CFA635Writer(cfa635, dataQueue);
            cfa635Thread = new Thread(cfa635Writer, "CFA635Writer");
        } catch (Exception e) {
            log.fatal("Error creating the needed threads", e);
            System.exit(1);
        }
        createTrayIcon();
    }

    /**
	 *  Starts the threads, and thus the application. Must be called after initialize() (no checks done).
	 */
    private void start() {
        try {
            ogThread.start();
        } catch (Exception e) {
            log.fatal("Couldn't start the OutGaugeReader thread", e);
            System.exit(1);
        }
        try {
            cfa635Thread.start();
        } catch (Exception e) {
            log.fatal("Couldn't start the CFA635Writer thread", e);
            System.exit(1);
        }
    }

    /**
	 * The main program entry.
	 * 
	 * @param args Command line arguments (none accepted currently)
	 */
    public static void main(String[] args) {
        log.info("Starting the application");
        LFSLCD lfslcd = new LFSLCD();
        lfslcd.initialize();
        lfslcd.start();
    }
}

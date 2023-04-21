package com.muddyhorse.cynch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.muddyhorse.cynch.gui.StandardButtonPanel;
import com.muddyhorse.cynch.gui.UpdateTablePanel;
import com.muddyhorse.cynch.gui.SelectedOps;
import com.muddyhorse.cynch.manifest.DownloadType;

/**
 *
 */
public class Cynch implements java.lang.Runnable, java.awt.event.ActionListener, SelectedOps.Listener {

    private Config cfg;

    private int countDownValue;

    private TextField txf;

    private volatile boolean stopped;

    private Thread myThread;

    private StandardButtonPanel buttonPanel;

    public Cynch(Config config, int countDown, TextField tf) {
        cfg = config;
        countDownValue = countDown;
        txf = tf;
    }

    public static void showFullGUI(Config cfg) {
        Frame f = new Frame(cfg.get(Constants.INI_UPD_FRAME_TITLE));
        UpdateUtils.setMainFrame(f);
        f.addWindowListener(Constants.CLOSING_ADAPTER);
        UpdateTablePanel updateTable = new UpdateTablePanel(cfg);
        SelectedOps selOps = updateTable.getSelectedOperations();
        StandardButtonPanel sbp = new StandardButtonPanel(cfg, selOps);
        Panel timeoutPanel = new Panel(new GridBagLayout());
        timeoutPanel.setBackground(Constants.CYNCH_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        Cynch cynch = addTimeoutText(cfg, timeoutPanel, gbc);
        f.add(timeoutPanel, BorderLayout.NORTH);
        f.add(updateTable, BorderLayout.CENTER);
        f.add(sbp, BorderLayout.SOUTH);
        cynch.setButtonPanel(sbp);
        sbp.addActionListener(cynch);
        selOps.addListener(cynch);
        f.setLocation(200, 100);
        f.pack();
        f.setVisible(true);
    }

    public static void showTimeoutDialog(Config cfg) {
        Frame f = new Frame(cfg.get(Constants.INI_UPD_FRAME_TITLE));
        f.addWindowListener(Constants.CLOSING_ADAPTER);
        UpdateUtils.setMainFrame(f);
        f.setResizable(false);
        f.setLayout(new GridBagLayout());
        f.setBackground(Constants.CYNCH_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        String s = cfg.getAppShortName();
        Label lbl = new Label(s + " (" + cfg.getAppDescription() + ")");
        f.add(lbl, gbc);
        lbl = new Label("There are no critical or required updates available.");
        gbc.gridy = 5;
        f.add(lbl, gbc);
        Cynch cy = addTimeoutText(cfg, f, gbc);
        Button b = new Button("Select optional updates...");
        b.setActionCommand(Constants.CMD_SELECT_OPTIONAL);
        b.addActionListener(cy);
        gbc.insets.top = 15;
        gbc.insets.bottom = 5;
        gbc.insets.left = 4;
        gbc.insets.right = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridy = 15;
        gbc.gridx = 0;
        f.add(b, gbc);
        if (cfg.isExitAllowed()) {
            b = new Button("Exit");
            b.setActionCommand(Constants.CMD_EXIT);
            b.addActionListener(cy);
            gbc.gridx = 2;
            f.add(b, gbc);
        }
        b = new Button("Run " + s);
        b.setActionCommand(Constants.CMD_RUN);
        b.addActionListener(cy);
        gbc.gridx = 1;
        f.add(b, gbc);
        f.pack();
        f.setLocation(200, 100);
        f.setVisible(true);
        b.requestFocus();
    }

    public static void showConnectErrorDialog(Config cfg) {
        Frame f = new Frame(cfg.get(Constants.INI_UPD_FRAME_TITLE));
        f.addWindowListener(Constants.CLOSING_ADAPTER);
        UpdateUtils.setMainFrame(f);
        f.setResizable(false);
        f.setLayout(new GridBagLayout());
        f.setBackground(Constants.CYNCH_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        String s = cfg.getAppShortName();
        Label lbl = new Label(s + " (" + cfg.getAppDescription() + ")");
        f.add(lbl, gbc);
        lbl = new Label("Unable to connect to update server(s).");
        gbc.gridy = 5;
        f.add(lbl, gbc);
        lbl = new Label("(Server addresses: " + cfg.get(Constants.INI_REMOTE_BASES) + ")");
        gbc.gridy = 6;
        f.add(lbl, gbc);
        Cynch cy = addTimeoutText(cfg, f, gbc);
        gbc.insets.top = 15;
        gbc.insets.bottom = 5;
        gbc.insets.left = 4;
        gbc.insets.right = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridy = 15;
        gbc.gridx = 2;
        Button b;
        if (cfg.isExitAllowed()) {
            b = new Button("Exit");
            b.setActionCommand(Constants.CMD_EXIT);
            b.addActionListener(cy);
            f.add(b, gbc);
        }
        b = new Button("Run " + s);
        b.setActionCommand(Constants.CMD_RUN);
        b.addActionListener(cy);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        f.add(b, gbc);
        f.pack();
        f.setLocation(200, 100);
        f.setVisible(true);
        b.requestFocus();
    }

    private static Cynch addTimeoutText(Config cfg, Container parent, GridBagConstraints gbc) {
        Label lbl = new Label("The application will be started in");
        gbc.gridy = 10;
        gbc.insets.top = 5;
        gbc.gridwidth = 1;
        parent.add(lbl, gbc);
        int actionTimeout = cfg.getActionTimeout();
        final TextField tf = new TextField(actionTimeout + Constants.SECONDS_SUFFIX);
        tf.setEditable(false);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        parent.add(tf, gbc);
        final Cynch cy = new Cynch(cfg, actionTimeout, tf);
        if (cfg.isTimeoutAbortAllowed()) {
            MouseAdapter mouseAdapter = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    cy.stop();
                    tf.removeMouseListener(this);
                }
            };
            parent.addMouseListener(mouseAdapter);
            tf.addMouseListener(mouseAdapter);
        }
        cy.start();
        return cy;
    }

    public void setButtonPanel(StandardButtonPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public void stop() {
        if (cfg.isTimeoutAbortAllowed()) {
            forceStop();
        }
    }

    private void forceStop() {
        stopped = true;
        if (myThread != null) {
            myThread.interrupt();
        }
        txf.setText("(Timer Disabled)");
    }

    public Thread start() {
        forceStop();
        myThread = new Thread(this);
        myThread.setName("Countdown thread");
        try {
            Thread.sleep(500);
            stopped = false;
            myThread.start();
        } catch (InterruptedException ex) {
            stopped = true;
        }
        return myThread;
    }

    public static void runApplicationAndExit(Config cfg) {
        UpdateUtils.startApplication(cfg);
        Frame f = UpdateUtils.getMainFrame();
        if (f != null) {
            f.setVisible(false);
            f.dispose();
        }
        System.exit(0);
    }

    public void run() {
        while (!stopped && countDownValue > 0) {
            txf.setText(Integer.toString(countDownValue) + Constants.SECONDS_SUFFIX);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                stopped = true;
            }
            --countDownValue;
        }
        if (!stopped) {
            if (buttonPanel != null && buttonPanel.isUpdateAvailable()) {
                buttonPanel.updateAndRun();
            } else {
                runApplicationAndExit(cfg);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        stop();
        String ac = e.getActionCommand();
        if (Constants.CMD_EXIT.equals(ac) && cfg.isExitAllowed()) {
            System.exit(0);
        } else if (Constants.CMD_RUN.equals(ac)) {
            runApplicationAndExit(cfg);
        } else if (Constants.CMD_SELECT_OPTIONAL.equals(ac)) {
            UpdateUtils.getMainFrame().setVisible(false);
            showFullGUI(cfg);
        }
    }

    public void selectedIDsChanged(boolean anySelected) {
        stop();
    }

    public static void main(String args[]) {
        try {
            String ini;
            if (args.length == 0) {
                ini = Constants.DEFAULT_INI_NAME;
            } else {
                ini = args[0];
            }
            Config cfg = new Config(ini, true);
            UpdateUtils.setMainConfig(cfg);
            long critSize = UpdateUtils.countDownloadSize(cfg, DownloadType.critical, null);
            long reqSize = UpdateUtils.countDownloadSize(cfg, DownloadType.required, null);
            long optSize = UpdateUtils.countDownloadSize(cfg, DownloadType.optional, null);
            if (critSize > 0 || reqSize > 0) {
                showFullGUI(cfg);
            } else if (optSize > 0) {
                showTimeoutDialog(cfg);
            } else if (!cfg.gotRemoteManifest()) {
                showConnectErrorDialog(cfg);
            } else {
                runApplicationAndExit(cfg);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}

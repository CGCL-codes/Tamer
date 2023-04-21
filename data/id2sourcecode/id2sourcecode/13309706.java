    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ((mySystem != null) && (!mySystem.getMyStatus().isStopped())) {
            mySystem.getMyStatus().setStopped(true);
            return;
        }
        if (cmdFileNewSimulation.equals(cmd)) {
            if (!checkSaved()) return;
            resetAll();
            return;
        }
        if (cmdFileOpenFile.equals(cmd)) {
            if (!checkSaved()) return;
            openFile();
            return;
        }
        if (cmdFileSaveConfiguration.equals(cmd)) {
            saveConfiguration();
            return;
        }
        if (cmdFileSaveSimulation.equals(cmd)) {
            saveSimulation();
            return;
        }
        if (cmdFileExit.equals(cmd)) {
            closeAndExit();
            return;
        }
        if (cmdEditSettings.equals(cmd)) {
            WindowSettings ws = new WindowSettingsHWC(mySystem, this);
            ws.setVisible(true);
            return;
        }
        if (cmdSimulationRun.equals(cmd)) {
            cmd2item.get(cmdSimulationRun).setEnabled(false);
            cmd2item.get(cmdSimulationStop).setEnabled(true);
            new Thread() {

                public void run() {
                    runSimulation();
                }
            }.start();
            return;
        }
        if (cmdHelpAbout.equals(cmd)) {
            new WindowAbout("Simulator", this);
            return;
        }
        if (cmdHelpContactTOPL.equals(cmd)) {
            Desktop desktop = null;
            String email = AuroraConstants.CONTACT_EMAIL;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.MAIL)) try {
                    desktop.mail(new URI("mailto", email, null));
                    return;
                } catch (Exception exp) {
                }
            }
            JOptionPane.showMessageDialog(this, "Cannot launch email client...\n Please, email your questions to\n" + email, "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        return;
    }

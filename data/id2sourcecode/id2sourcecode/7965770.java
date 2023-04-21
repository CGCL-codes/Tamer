    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ((mySystem != null) && (!mySystem.getMyStatus().isStopped())) {
            mySystem.getMyStatus().setStopped(true);
            return;
        }
        if (cmdFileNewConfiguration.equals(cmd)) {
            if (!checkSaved()) return;
            newConfiguration();
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
        if (cmdFileExit.equals(cmd)) {
            closeAndExit();
            return;
        }
        if (cmdEditSettings.equals(cmd)) {
            WindowSettings ws = new WindowSettingsHWC(mySystem, null);
            ws.setVisible(true);
            return;
        }
        if (cmdViewFilter.equals(cmd)) {
            WindowFilter wf = new WindowFilter(treePane, null);
            wf.setVisible(true);
            return;
        }
        if (cmdToolsConfigurationSummary.equals(cmd)) {
            ConfigurationSummaryHWC cs = new ConfigurationSummaryHWC();
            if ((mySystem != null) && (mySystem.getMyNetwork() != null)) {
                mySystem.getMyNetwork().updateConfigurationSummary(cs);
                new WindowConfigurationSummary(cs, this);
            }
            return;
        }
        if (cmdToolsValidate.equals(cmd)) {
            if ((mySystem != null) && (mySystem.getMyNetwork() != null)) {
                try {
                    mySystem.getMyNetwork().validate();
                } catch (Exception exp) {
                    String buf = exp.getMessage();
                    if ((buf == null) || (buf.equals(""))) buf = "Unknown error...";
                    JOptionPane.showMessageDialog(this, buf, exp.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
                }
                treePane.getActionPane().updateErrorTable();
            }
            return;
        }
        if (cmdHelpAbout.equals(cmd)) {
            new WindowAbout("Configurator", this);
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

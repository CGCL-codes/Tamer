    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmdFileOpenFile.equals(cmd)) {
            openFile(null);
            return;
        }
        if (cmdFilterType.equals(cmd)) {
            typeFilter();
            return;
        }
        if (cmdFilterName.equals(cmd)) {
            nameFilter();
            return;
        }
        if (cmdFilterSimplify.equals(cmd)) {
            simplifyEdges();
            return;
        }
        if (cmdFileSaveAs.equals(cmd)) {
            exportToGIS();
            return;
        }
        if (cmdFileSaveConfiguration.equals(cmd)) {
            exportToXML();
            return;
        }
        if (cmdHelpAbout.equals(cmd)) {
            new WindowAbout("GISImporter", this);
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
        if (cmdFileExit.equals(cmd)) {
            statusBar.setText("Exiting...");
            setVisible(false);
            dispose();
            System.exit(0);
        }
    }

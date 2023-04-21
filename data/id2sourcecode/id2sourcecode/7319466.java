    private void itemOpenConfigActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            File config = dc.getConfigFile();
            if (config.exists() && config.length() > 0) {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                    desktop.edit(config);
                }
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

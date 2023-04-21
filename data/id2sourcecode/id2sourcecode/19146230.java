    private void jButton_downloadActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            instancePanel.panel2Variables();
            String clientFileName = jTextField_clientFileName.getText();
            File clientFile = new File(clientFileName);
            if (clientFile.exists()) {
                String message = "File " + clientFile.getAbsolutePath() + " already exists!\n" + "Overwrite it?";
                int n = JOptionPane.showConfirmDialog(this, message, "Overwrite file?", JOptionPane.YES_NO_OPTION);
                if (n != 0) {
                    return;
                }
            }
            instanceProxy.download(attrib.getName());
        } catch (Exception ex) {
            gui.handleException(ex);
        }
    }

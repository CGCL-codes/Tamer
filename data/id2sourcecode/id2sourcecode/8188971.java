    @SuppressWarnings({ "UnusedDeclaration" })
    private void saveFileActionPerformed(java.awt.event.ActionEvent evt) {
        if (signals.size() == 0) {
            Utils.popupError(this, "No subscription to save");
            return;
        }
        if (chooser == null) {
            String homeDir;
            if ((homeDir = System.getProperty("EVT_DATA_FILES")) == null) homeDir = new File("").getAbsolutePath();
            chooser = new JFileChooser(homeDir);
        }
        chooser.setDialogTitle("Save Configuration");
        chooser.setApproveButtonText("Save");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null && !file.isDirectory()) {
                if (file.exists()) if (JOptionPane.showConfirmDialog(this, "This File Already Exists !\n\n" + "Would you like to overwrite ?", "information", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                saveEventList(file.getAbsolutePath());
            }
        }
    }

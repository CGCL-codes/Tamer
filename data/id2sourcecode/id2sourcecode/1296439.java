    @SuppressWarnings({ "UnusedDeclaration" })
    private void saveItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (globalStatistics == null) return;
        initChooser("Save");
        int retval = chooser.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                if (!file.isDirectory()) {
                    String fileName = file.getAbsolutePath();
                    if (!fileName.endsWith(".xml")) fileName += ".xml";
                    if (new File(fileName).exists()) {
                        if (JOptionPane.showConfirmDialog(this, fileName + "   File already exists\n\n     Overwrite it ?", "Confirm Dialog", JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION) {
                            return;
                        }
                    }
                    try {
                        globalStatistics.saveStatistics(fileName);
                    } catch (DevFailed e) {
                        ErrorPane.showErrorMessage(this, null, e);
                    }
                }
            }
        }
    }

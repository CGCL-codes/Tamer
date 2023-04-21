    public void buttonActionPerformed(java.awt.event.ActionEvent ae) {
        if (ae.getSource() == backupButton) {
            log.debug("backup button activated");
            if (CarManagerXml.instance().isDirty() || EngineManagerXml.instance().isDirty() || TrainManagerXml.instance().isDirty()) {
                if (JOptionPane.showConfirmDialog(this, rb.getString("OperationsFilesModified"), rb.getString("SaveOperationFiles"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    TrainManager.instance().save();
                }
            }
            if (backup.checkDirectoryExists(backupTextField.getText())) {
                if (JOptionPane.showConfirmDialog(this, MessageFormat.format(rb.getString("DirectoryAreadyExists"), new Object[] { backupTextField.getText() }), rb.getString("OverwriteBackupDirectory"), JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            boolean success = backup.backupFiles(backupTextField.getText());
            if (success) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not backup operation files", "Backup failed!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

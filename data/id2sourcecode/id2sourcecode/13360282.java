    private void myExportInstallerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int zResult = myFileChooser.showSaveDialog(this);
            if (zResult == JFileChooser.APPROVE_OPTION) {
                boolean zProceed = true;
                if (myFileChooser.getSelectedFile().exists()) {
                    int zOverwriteResult = JOptionPane.showConfirmDialog(this, "" + myFileChooser.getSelectedFile().getName() + " exists.  Overwrite?", "File already exists", JOptionPane.YES_NO_OPTION);
                    if (zOverwriteResult != JOptionPane.YES_OPTION) {
                        zProceed = false;
                    }
                }
                if (zProceed == true) {
                    String zXMLString = REPO_XML_HEADER + myRepository.toXMLString();
                    FileWriter zFW = new FileWriter(myFileChooser.getSelectedFile());
                    zFW.write(zXMLString);
                    zFW.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

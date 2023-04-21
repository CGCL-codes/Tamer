    public void doOK() {
        if (!checkRootDN()) return;
        setVisible(false);
        JFileChooser chooser = new JFileChooser(JXConfig.getProperty("ldif.homeDir"));
        chooser.addChoosableFileFilter(new CBFileFilter(new String[] { "ldif", "ldi" }, "Ldif Files (*.ldif, *.ldi)"));
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File readFile = chooser.getSelectedFile();
            if (readFile == null) {
                CBUtility.error(CBIntText.get("Please select a file"));
            } else {
                readFile = adjustFileName(readFile);
                int response = -1;
                if (readFile.exists()) {
                    response = JOptionPane.showConfirmDialog(this, CBIntText.get("File ''{0}'' already exsists.  Do you want to replace it?", new String[] { readFile.toString() }), CBIntText.get("Overwrite Confirmation"), JOptionPane.YES_NO_OPTION);
                    if (response != JOptionPane.YES_OPTION) {
                        setVisible(true);
                        return;
                    }
                }
                JXConfig.setProperty("ldif.homeDir", readFile.getParent());
                doFileWrite(readFile);
            }
        }
    }

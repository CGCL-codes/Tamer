    public boolean exportPreferencesDialog() {
        boolean done = false;
        while (!done) {
            JFileChooser chooser = new JFileChooser();
            XMLFileFilter filter = new XMLFileFilter();
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(lastFile);
            chooser.setApproveButtonText("Export bias settings");
            int retValue = chooser.showSaveDialog(this);
            if (retValue == JFileChooser.CANCEL_OPTION) {
                return false;
            } else if (retValue == JFileChooser.APPROVE_OPTION) {
                try {
                    lastFile = chooser.getSelectedFile();
                    if (!lastFile.getName().endsWith(XMLFileFilter.EXTENSION)) {
                        lastFile = new File(lastFile.getCanonicalPath() + XMLFileFilter.EXTENSION);
                    }
                    if (lastFile.exists()) {
                        int retVal = JOptionPane.showConfirmDialog(this, lastFile + " already exists, overwrite it?", "Overwrite file?", JOptionPane.OK_CANCEL_OPTION);
                        if (retVal == JOptionPane.CANCEL_OPTION) {
                            continue;
                        }
                    }
                    exportPreferencesToFile(lastFile);
                    done = true;
                    prefs.put("BiasgenFrame.lastFile", lastFile.toString());
                    recentFiles.addFile(lastFile);
                    return true;
                } catch (Exception fnf) {
                    setStatusMessage(fnf.getMessage());
                    log.warning(fnf.toString());
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    return false;
                }
            }
        }
        return true;
    }

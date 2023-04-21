    public void doSaveTreeAs() {
        try {
            JFileChooser fc = referenceManager.getDefaultFileChooser();
            fc.setSelectedFile(gametreeFile);
            int result = fc.showSaveDialog(referenceManager.getMainFrame());
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                if (f.exists()) {
                    String msg = MessageFormat.format("File {0} already exists.\nDo you wish to overwrite it?", new Object[] { f.getCanonicalPath() });
                    if (JOptionPane.YES_OPTION != JOptionPane.showInternalConfirmDialog(referenceManager.getMainFrame().getDesktop(), msg, "Overwrite File?", JOptionPane.YES_NO_OPTION)) return;
                }
                gametreeFile = f;
                doSaveTree();
            }
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }

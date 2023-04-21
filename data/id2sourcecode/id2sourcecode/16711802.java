    private File selectOutputFile(Alignment alignment, String defaultFilename, ExtensionFilter filter) {
        File currentDir = new File(".");
        String lastFilePath = AppPreferences.store.get(AppPreferences.PROP_LAST_EXTRACT_OUTPUT_FILE, null);
        if (lastFilePath == null) {
            lastFilePath = AppPreferences.store.get(AppPreferences.PROP_LAST_ALIGNMENT_FILE, null);
        }
        if (lastFilePath != null) {
            File lastFile = new File(lastFilePath);
            if (lastFile.getParentFile().exists()) {
                currentDir = lastFile.getParentFile();
            }
        }
        File outFile = null;
        while (outFile == null) {
            File currentFile = new File(currentDir, defaultFilename);
            JFileChooser fc = new JFileChooser(currentDir);
            fc.addChoosableFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setSelectedFile(currentFile);
            int retValue = fc.showSaveDialog(app.getWindow());
            if (retValue != JFileChooser.APPROVE_OPTION) {
                return null;
            }
            outFile = fc.getSelectedFile();
            if ((outFile != null) && (outFile.exists())) {
                int option = JOptionPane.showConfirmDialog(app.getWindow(), "File " + outFile + " already exists. Are you sure you want to overwrite?", "Confirm file overwrite", JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) {
                    outFile = null;
                }
            }
        }
        return outFile;
    }

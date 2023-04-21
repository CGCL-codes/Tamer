    public void commitChanges(CurationSet curation, boolean saveAnnots, boolean saveResults) {
        if (getInputType() != DataInputType.FILE) {
            apollo.main.DataLoader loader = new apollo.main.DataLoader();
            loader.saveFileDialog(curation);
            return;
        }
        String filename = apollo.util.IOUtil.findFile(getInput(), true);
        if (filename == null) filename = getInput();
        if (filename == null) return;
        if (Config.getConfirmOverwrite()) {
            File handle = new File(filename);
            if (handle.exists()) {
                if (!LoadUtil.areYouSure(filename + " already exists--overwrite?")) {
                    apollo.main.DataLoader loader = new apollo.main.DataLoader();
                    loader.saveFileDialog(curation);
                    return;
                } else {
                    logger.info("GAMEAdapter overwriting existing file " + filename);
                }
            }
        }
        String msg = "Saving data to file " + filename;
        setInput(filename);
        fireProgressEvent(new ProgressEvent(this, new Double(10.0), msg));
        if (GAMESave.writeXML(curation, filename, saveAnnots, saveResults, "Apollo version: " + Version.getVersion(), false)) {
            saveTransactions(curation, filename);
        } else {
            String message = "Failed to save GAME XML to " + filename;
            logger.error(message);
            JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

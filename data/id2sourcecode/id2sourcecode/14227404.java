    public void commitChanges(CurationSet curation) {
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
                }
            }
        }
        setInput(filename);
        String msg = "Saving data to file " + filename;
        fireProgressEvent(new ProgressEvent(this, new Double(10.0), msg));
        if (DAS2Writer.writeXML(curation, filename, "Apollo version: " + apollo.main.Version.getVersion())) {
            logger.info("Saved DAS2XML to " + filename);
        } else {
            String message = "Failed to save DAS2XML to " + filename;
            logger.error(message);
            JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

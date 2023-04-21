    public void commitChanges(CurationSet curation, boolean saveAnnots, boolean saveResults) {
        String filename = apollo.util.IOUtil.findFile(getInput(), true);
        if (filename == null) filename = getInput();
        if (filename == null) return;
        String msg = "Retrieving preamble from original file... ";
        fireProgressEvent(new ProgressEvent(this, new Double(5.0), msg));
        String preamble = getPreamble(curation.getInputFilename());
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
        msg = "Saving Chado XML to file " + filename + "... ";
        fireProgressEvent(new ProgressEvent(this, new Double(20.0), msg));
        if (ChadoXmlWrite.writeXML(curation, filename, preamble, saveAnnots, saveResults, getNameAdapter(curation), "Apollo version: " + Version.getVersion())) {
            logger.info("Saved Chado XML to " + filename);
            ChadoXmlWrite.saveTransactions(curation, filename);
        } else {
            String message = "Failed to save Chado XML to " + filename;
            logger.error(message);
            JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

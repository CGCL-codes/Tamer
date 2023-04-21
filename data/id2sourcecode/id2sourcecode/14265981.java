    public static boolean save(WabitSwingSessionContext context, WabitSwingSession session) {
        if (session == null) return false;
        boolean promptForFile = true;
        File selectedFile = null;
        while (promptForFile) {
            JFileChooser fc = new JFileChooser(session.getCurrentURIAsFile());
            fc.setDialogTitle("Select the directory to save " + session.getWorkspace().getName() + " to.");
            fc.addChoosableFileFilter(SPSUtils.WABIT_FILE_FILTER);
            int fcChoice = fc.showSaveDialog(context.getFrame());
            if (fcChoice != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            selectedFile = updateFileName(fc.getSelectedFile());
            if (selectedFile.exists()) {
                int response = JOptionPane.showConfirmDialog(context.getFrame(), "The file " + selectedFile.getName() + " already exists, overwrite?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    promptForFile = false;
                }
            } else {
                promptForFile = false;
            }
        }
        try {
            session.getWorkspace().generateNewUUID();
            selectedFile = saveSessionToFile(context, session, selectedFile);
        } catch (SaveException e) {
            JOptionPane.showMessageDialog(context.getFrame(), e.getMessage(), "Error on Saving", JOptionPane.ERROR_MESSAGE);
            context.setStatusMessage(e.getMessage());
            return false;
        }
        context.setStatusMessage("Saved " + session.getWorkspace().getName() + " to " + selectedFile.getName());
        return true;
    }

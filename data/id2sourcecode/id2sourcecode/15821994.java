    private int askForOverwrite(final File destinationFile) {
        return JOptionPane.showConfirmDialog(myMainFrame, "The file '" + destinationFile.getName() + "' already exists.  Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

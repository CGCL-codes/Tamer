    private void validate() {
        boolean ok = true;
        File ldifFile = new File(ldifFileBrowserWidget.getFilename());
        if ("".equals(ldifFileBrowserWidget.getFilename())) {
            setErrorMessage(null);
            ok = false;
        } else if (!ldifFile.isFile() || !ldifFile.exists()) {
            setErrorMessage("Selected LDIF file doesn't exist.");
            ok = false;
        } else if (!ldifFile.canRead()) {
            setErrorMessage("Selected LDIF file is not readable.");
            ok = false;
        } else if (this.enableLoggingButton.getSelection()) {
            File logFile = new File(logFileBrowserWidget.getFilename());
            File logFileDirectory = logFile.getParentFile();
            if (logFile.equals(ldifFile)) {
                setErrorMessage("LDIF file and Logfile must not be equal.");
                ok = false;
            } else if (logFile.isDirectory()) {
                setErrorMessage("Selected logfile is no file.");
                ok = false;
            } else if (logFile.exists() && !this.overwriteLogfileButton.getSelection()) {
                setErrorMessage("Selected logfile already exists. Select option 'Overwrite existing logfile' if you want to overwrite the logfile.");
                ok = false;
            } else if (logFile.exists() && !logFile.canWrite()) {
                setErrorMessage("Selected logfile is not writeable.");
                ok = false;
            } else if (logFile.getParentFile() == null) {
                setErrorMessage("Selected logfile directory is not writeable.");
                ok = false;
            } else if (!logFile.exists() && (logFileDirectory == null || !logFileDirectory.canWrite())) {
                setErrorMessage("Selected logfile directory is not writeable.");
                ok = false;
            }
        }
        if (wizard.getImportConnection() == null) {
            ok = false;
        }
        if (ok) {
            setErrorMessage(null);
        }
        setPageComplete(ok);
        getContainer().updateButtons();
    }

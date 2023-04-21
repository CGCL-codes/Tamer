    protected void validate() {
        boolean ok = true;
        File file = new File(fileBrowserWidget.getFilename());
        File fileDirectory = file.getParentFile();
        if ("".equals(fileBrowserWidget.getFilename())) {
            setErrorMessage(null);
            ok = false;
        } else if (file.isDirectory()) {
            setErrorMessage("Selected " + getFileType() + " is no file.");
            ok = false;
        } else if (file.exists() && !this.overwriteFileButton.getSelection()) {
            setErrorMessage("Selected " + getFileType() + " file already exists. Select option 'Overwrite existing " + getFileType() + " file' if you want to overwrite the " + getFileType() + " file.");
            ok = false;
        } else if (file.exists() && !file.canWrite()) {
            setErrorMessage("Selected " + getFileType() + " file is not writeable.");
            ok = false;
        } else if (file.getParentFile() == null) {
            setErrorMessage("Selected " + getFileType() + " file directory is not writeable.");
            ok = false;
        } else if (!file.exists() && (fileDirectory == null || !fileDirectory.canWrite())) {
            setErrorMessage("Selected " + getFileType() + " file directory is not writeable.");
            ok = false;
        }
        if (ok) {
            setErrorMessage(null);
        }
        setPageComplete(ok && wizard.getExportFilename() != null && !"".equals(wizard.getExportFilename()));
    }

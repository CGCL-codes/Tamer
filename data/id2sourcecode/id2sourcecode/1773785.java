    @Override
    public void run() {
        this.myProgressBar.setString("Reading Directories");
        this.myProgressBar.setValue(0);
        clsPrefs.putBoolean(ZIP_FILE, this.myZipFiles.isSelected());
        clsPrefs.putBoolean(SUB_DIR, this.myRename.isSelected());
        clsPrefs.putBoolean(MOVE_FILES, this.myMoveFilesToRootDirectory.isSelected());
        clsPrefs.put(PATTERN, this.myPattern.getText());
        clsPrefs.putBoolean(USE_DIR_NAMES, this.iUseDirectoryNames.isSelected());
        clsPrefs.put(IGNORE, this.myIgnore.getText());
        this.myRootString = this.myRootDir.getText();
        File rootDir = new File(this.myRootString);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Root directory must be a directory", "Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            this.myLogger = EzLogger.makeEzLogger(rootDir);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open log file in:\n " + rootDir, "Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (this.myZipFiles.isSelected()) {
                unzipFiles(rootDir.listFiles(new EndingFileFilter("zip")));
            }
            if (this.myRename.isSelected() || this.myMoveFilesToRootDirectory.isSelected()) {
                ListSubdirectories list = new ListSubdirectories(rootDir);
                if (this.myRename.isSelected()) {
                    renameLoop(rootDir, list);
                }
                if (this.myMoveFilesToRootDirectory.isSelected()) {
                    moveFilesToTop(rootDir, list);
                    deleteEmptyDirectories(rootDir);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Problem Encountered", JOptionPane.ERROR_MESSAGE);
        }
        try {
            this.myLogger.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        this.myProgressBar.setString("Done");
    }

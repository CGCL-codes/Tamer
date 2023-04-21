    private synchronized void backupProperties() {
        try {
            FileUtils.copyFile(getPreferencesFile(), getBackupFile());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Problem backing up JSystem properties file");
        }
    }

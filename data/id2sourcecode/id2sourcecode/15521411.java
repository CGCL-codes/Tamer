    public synchronized boolean restoreFromBackup() {
        if (!isBackupValid()) {
            return false;
        }
        log.log(Level.SEVERE, "JSystem properties file was empty, restoring from backup.");
        File bu = getBackupFile();
        try {
            FileUtils.copyFile(bu, getPreferencesFile());
            return true;
        } catch (IOException e) {
            log.log(Level.SEVERE, "Problem restoring JSystem properties from backup");
            return false;
        }
    }

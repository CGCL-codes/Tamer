    private void createBackupFolder() throws ConfigurationException {
        File backupFolder = getBackupFolder();
        try {
            boolean success = backupFolder.mkdirs();
            if (!success) {
                throw new ConfigurationException("Unable to create required directories for ''{0}''.", backupFolder.getAbsolutePath());
            } else {
                cacheLog.debug("Created cache backup folder for account {0} (Users: {1}).", account.getOid(), account.getUsers());
            }
        } catch (SecurityException e) {
            throw new ConfigurationException("Security manager has denied read/write access to local user cache in ''{0}'' : {1}", e, backupFolder.getAbsolutePath(), e.getMessage());
        }
    }

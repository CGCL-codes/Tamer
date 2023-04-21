    private void createCacheFolder() throws ConfigurationException {
        try {
            boolean success = cacheFolder.mkdirs();
            if (!success) {
                throw new ConfigurationException("Unable to create required directories for ''{0}''.", cacheFolder.getAbsolutePath());
            } else {
                cacheLog.info("Created account {0} cache folder (Users: {1}).", account.getOid(), account.getUsers());
            }
        } catch (SecurityException e) {
            throw new ConfigurationException("Security manager has denied read/write access to local user cache in ''{0}'' : {1}", e, cacheFolder.getAbsolutePath(), e.getMessage());
        }
        if (!hasBackupFolder()) {
            createBackupFolder();
        }
    }

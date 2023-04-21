    private void init(EnvironmentImpl envImpl, long lastFileInPrevBackup) throws DatabaseException {
        this.envImpl = envImpl;
        FileManager fileManager = envImpl.getFileManager();
        envIsReadOnly = fileManager.checkEnvHomePermissions(true);
        if ((!envIsReadOnly) && envImpl.isReadOnly()) {
            throw new IllegalArgumentException("Environment handle may not be read-only when directory " + "is read-write");
        }
        firstFileInBackup = lastFileInPrevBackup + 1;
    }

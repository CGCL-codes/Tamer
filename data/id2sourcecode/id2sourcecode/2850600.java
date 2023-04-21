    public boolean checkEnvHomePermissions(boolean readOnly) throws DatabaseException {
        boolean envDirIsReadOnly = !dbEnvHome.canWrite();
        if (envDirIsReadOnly && !readOnly) {
            throw new DatabaseException("The Environment directory " + dbEnvHome.getAbsolutePath() + " is not writable, but the " + "Environment was opened for read-write access.");
        }
        return envDirIsReadOnly;
    }

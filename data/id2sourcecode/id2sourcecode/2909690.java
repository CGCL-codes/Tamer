    public boolean checkEnvHomePermissions(boolean rdOnly) throws DatabaseException {
        boolean envDirIsReadOnly = !dbEnvHome.canWrite();
        if (envDirIsReadOnly && !rdOnly) {
            throw new IllegalArgumentException("The Environment directory " + dbEnvHome.getAbsolutePath() + " is not writable, but the " + "Environment was opened for read-write access.");
        }
        return envDirIsReadOnly;
    }

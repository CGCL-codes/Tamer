    public DbBackup(Environment env) throws DatabaseException {
        env.checkHandleIsValid();
        envImpl = DbInternal.envGetEnvironmentImpl(env);
        FileManager fileManager = envImpl.getFileManager();
        envIsReadOnly = fileManager.checkEnvHomePermissions(true);
        if ((!envIsReadOnly) && envImpl.isReadOnly()) {
            throw new DatabaseException(this.getClass().getName() + " requires a read/write Environment handle");
        }
    }

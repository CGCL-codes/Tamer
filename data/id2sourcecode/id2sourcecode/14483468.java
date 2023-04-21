    protected boolean canReadDataDir(Configuration conf) throws EXistException {
        String dataDir = (String) conf.getProperty(PROPERTY_DATA_DIR);
        if (dataDir == null) dataDir = "data";
        File dir = new File(dataDir);
        if (!dir.exists()) {
            try {
                LOG.info("Data directory '" + dir.getAbsolutePath() + "' does not exist. Creating one ...");
                dir.mkdirs();
            } catch (SecurityException e) {
                LOG.info("Cannot create data directory '" + dir.getAbsolutePath() + "'. Switching to read-only mode.");
                return false;
            }
        }
        conf.setProperty(PROPERTY_DATA_DIR, dataDir);
        if (!dir.canWrite()) {
            LOG.info("Cannot write to data directory: " + dir.getAbsolutePath() + ". Switching to read-only mode.");
            return false;
        }
        dataLock = new FileLock(this, dir, "dbx_dir.lck");
        try {
            boolean locked = dataLock.tryLock();
            if (!locked) {
                throw new EXistException("The database directory seems to be locked by another " + "database instance. Found a valid lock file: " + dataLock.getFile());
            }
        } catch (ReadOnlyException e) {
            LOG.info(e.getMessage() + ". Switching to read-only mode!!!");
            return false;
        }
        return true;
    }

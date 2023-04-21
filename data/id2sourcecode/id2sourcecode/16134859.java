    public boolean createLock() {
        try {
            File lockFile = new File(instancePath, LOCK_FILENAME);
            if (lockFile.exists() && lockFile.isFile()) {
                RandomAccessFile rw = new RandomAccessFile(lockFile, "rw");
                fileLock = rw.getChannel().tryLock();
                if (fileLock == null) {
                    Logger.log(IStatus.INFO, "Another PubCurator instance is already running.");
                    showMessage("Another PubCurator instance is already running.\nNo second instance is allowed for database consistency reasons.");
                    return false;
                } else {
                    Logger.log(IStatus.INFO, "A prior running instance of PubCurator crashed.");
                }
            } else {
                if (lockFile.getParentFile() != null && !lockFile.getParentFile().isDirectory()) {
                    lockFile.getParentFile().mkdirs();
                }
                lockFile.createNewFile();
                RandomAccessFile rw = new RandomAccessFile(lockFile, "rw");
                fileLock = rw.getChannel().lock();
            }
        } catch (Exception e) {
            Logger.log(IStatus.ERROR, "A PubCurator lock file could not be created.", e);
            showMessage("Error while trying creating a lock file.\nPlease make sure that your PubCurator directory is writable.");
            return false;
        }
        return true;
    }

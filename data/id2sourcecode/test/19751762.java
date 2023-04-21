    private FileLock acquireLock(FileOutputStream out) {
        FileLock lock = null;
        try {
            lock = out.getChannel().lock();
        } catch (Exception e) {
            e.printStackTrace();
            errorHandler.showConfigFileLockErrorMessage();
            return null;
        }
        return lock;
    }

    private void checkIfApplicationIsAlreadyRunningAndLockFile() throws IOException {
        lockFile = new File(getConfig().getBufferDirectory(), "qedeq_lock.lck");
        FileLock fl = null;
        try {
            lockStream = new FileOutputStream(lockFile);
            lockStream.write("LOCKED".getBytes("UTF8"));
            lockStream.flush();
            fl = lockStream.getChannel().tryLock();
        } catch (IOException e) {
            throw new IOException("It seems the application is already running.\n" + "At least accessing the file \"" + lockFile.getAbsolutePath() + "\" failed.");
        }
        if (fl == null) {
            throw new IOException("It seems the application is already running.\n" + "At least locking the file \"" + lockFile.getAbsolutePath() + "\" failed.");
        }
    }

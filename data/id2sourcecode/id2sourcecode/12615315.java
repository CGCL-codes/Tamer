    private void lockDb() throws IOException {
        lockFile = new File(fDir, LOCK_FILE_NAME);
        if (!lockFile.exists()) lockFile.createNewFile();
        lockFile.deleteOnExit();
        lockFileOutputStream = new FileOutputStream(lockFile);
        lockFileLock = lockFileOutputStream.getChannel().tryLock();
        if (lockFileLock == null) {
            lockFileOutputStream.close();
            throw new IOException("Database is already in use.");
        }
    }

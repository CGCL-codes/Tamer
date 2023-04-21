    public static FileLock lockFile(RandomAccessFile fileToLock, String fileToLockPath, int maxRetryCount) throws IOException {
        FileChannel fileChannel = fileToLock.getChannel();
        for (int numTries = 0; numTries < maxRetryCount; numTries++) {
            try {
                return fileChannel.lock();
            } catch (OverlappingFileLockException e) {
                logger.info("Could not obtain lock on " + fileToLockPath + " numTries=" + numTries);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
        throw new RuntimeException("Failed to obtain lock on " + fileToLockPath);
    }

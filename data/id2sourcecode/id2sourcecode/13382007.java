    public int getNextItemId() {
        int nextItemId;
        FileChannel lockChannel = idFile.getChannel();
        FileLock repositoryLock = null;
        while (repositoryLock == null && !Thread.currentThread().isInterrupted()) {
            try {
                repositoryLock = lockChannel.tryLock();
            } catch (IOException e) {
            }
            if (repositoryLock == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.warn("Interrupted waiting to obtain repository id lock.");
                    continue;
                }
            }
        }
        if (repositoryLock == null) {
            throw new RuntimeException("Failed to obtain repository id lock.");
        }
        try {
            idFile.seek(0);
            nextItemId = idFile.readInt();
            idFile.seek(0);
            idFile.writeInt(nextItemId + 1);
        } catch (IOException e) {
            repositoryLock = null;
            throw new RuntimeException("Failed to read/write next repository item id.", e);
        } finally {
            try {
                repositoryLock.release();
            } catch (IOException ne) {
                log.warn("Error occurred releasing lock after id read/write.", ne);
            }
        }
        return nextItemId;
    }

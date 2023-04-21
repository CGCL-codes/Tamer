    private boolean isApplicationRunning() {
        File applicationLockFile = new File(Options.getLockFile().getParentFile(), ".applicationlock");
        RandomAccessFile randomAccessFile = null;
        FileLock applicationLock = null;
        try {
            randomAccessFile = new RandomAccessFile(applicationLockFile, "rw");
            applicationLock = randomAccessFile.getChannel().tryLock();
        } catch (IOException ioe) {
        } finally {
            if (applicationLock != null) {
                try {
                    applicationLock.release();
                } catch (IOException ioe) {
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException ioe) {
                }
            }
            if (Options.isDebug()) {
                System.out.println("isApplicationRunning? " + (applicationLock == null));
            }
        }
        return applicationLock == null;
    }

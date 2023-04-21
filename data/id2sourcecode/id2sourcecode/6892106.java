    public ConcurrencyLock(String directory, int port, String timeStamp) {
        lockFile = new File(directory, LOCK_FILE_NAME);
        infoFile = new File(directory, INFO_FILE_NAME);
        try {
            lockChannel = new FileOutputStream(lockFile).getChannel();
            lock = lockChannel.tryLock();
            if (lock != null) {
                writeLockInfo(port, timeStamp);
                registerShutdownHook();
            } else {
                if (notifyOtherDashboard()) System.exit(0); else showReadOnlyOptionDialog(getPath(lockFile));
            }
        } catch (IOException e) {
            if (lockChannel == null || lock != null) showFailureDialog(getPath(lockFile)); else showWarningDialog(getPath(lockFile));
            unlock();
            System.exit(0);
        }
    }

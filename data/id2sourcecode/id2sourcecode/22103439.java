    private void lockFile(long lockWaitTime, long lockRetryPeriod) throws IOException {
        long entryTime = System.currentTimeMillis();
        FileChannel channel = file.getChannel();
        lock = channel.tryLock(0, Long.MAX_VALUE, false);
        if (lock != null) {
            counters.registerQuickLock();
            return;
        }
        do {
            try {
                Thread.sleep(lockRetryPeriod);
            } catch (InterruptedException e) {
            }
            lock = channel.tryLock(0, Long.MAX_VALUE, false);
            if (lock != null) {
                counters.registerDelayedLock();
                return;
            }
        } while (System.currentTimeMillis() - entryTime <= lockWaitTime);
        counters.registerError();
        throw new IOException("Could not obtain exclusive lock on file: " + getPath() + "] after " + lockWaitTime + " milliseconds");
    }

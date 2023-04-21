    private void lockFile(final long lockWaitTime, final long lockRetryPeriod) throws IOException {
        final long entryTime = System.currentTimeMillis();
        final FileChannel channel = file.getChannel();
        m_lock = channel.tryLock(0, Long.MAX_VALUE, false);
        if (m_lock != null) {
            counters.registerQuickLock();
            return;
        }
        do {
            try {
                Thread.sleep(lockRetryPeriod);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            m_lock = channel.tryLock(0, Long.MAX_VALUE, false);
            if (m_lock != null) {
                counters.registerDelayedLock();
                return;
            }
        } while (System.currentTimeMillis() - entryTime <= lockWaitTime);
        counters.registerError();
        throw new IOException("Could not obtain exclusive m_lock on file: " + getPath() + "] after " + lockWaitTime + " milliseconds");
    }

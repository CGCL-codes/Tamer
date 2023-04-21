        private void force() throws DatabaseException, IOException {
            fsyncFileSynchronizer.lock();
            try {
                if (useWriteQueue) {
                    dequeuePendingWrites1();
                }
                RandomAccessFile file = endOfLogSyncFile;
                if (file != null) {
                    bumpWriteCount("fsync");
                    FileChannel ch = file.getChannel();
                    try {
                        ch.force(false);
                        nLogFSyncs.increment();
                    } catch (ClosedChannelException e) {
                        throw new ThreadInterruptedException(envImpl, "Channel closed, may be due to thread interrupt", e);
                    }
                    assert EnvironmentImpl.maybeForceYield();
                }
                if (useWriteQueue) {
                    dequeuePendingWrites1();
                }
            } finally {
                fsyncFileSynchronizer.unlock();
            }
        }

        void force() throws DatabaseException, IOException {
            synchronized (fsyncFileSynchronizer) {
                RandomAccessFile file = endOfLogSyncFile;
                if (file != null) {
                    FileChannel channel = file.getChannel();
                    try {
                        channel.force(false);
                    } catch (ClosedChannelException e) {
                        throw new RunRecoveryException(envImpl, "Channel closed, may be due to thread interrupt", e);
                    }
                    assert EnvironmentImpl.maybeForceYield();
                }
            }
        }

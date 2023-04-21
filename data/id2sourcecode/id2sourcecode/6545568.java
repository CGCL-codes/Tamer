    synchronized FileChannel getChannel(boolean rw) throws IOException {
        updateTimestamp();
        if (null != channel) {
            if (rw && !channelIsRW) {
                if (logger.isDebugEnabled()) logger.debug("Promoting channel for " + file + " to rw.");
                synchronized (channel) {
                    channel.close();
                }
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                channel = raf.getChannel();
            }
        } else {
            if (logger.isDebugEnabled()) logger.debug("Opening channel for " + file + ". rw=" + rw);
            RandomAccessFile raf = new RandomAccessFile(file, rw ? "rw" : "r");
            channel = raf.getChannel();
            channelIsRW = rw;
            CacheCleaner.registerDirtyFile(this);
        }
        return channel;
    }

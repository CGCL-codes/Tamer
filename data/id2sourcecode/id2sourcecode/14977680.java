    public void changeNetworkLimit(long writeGlobalLimit, long readGlobalLimit, long writeSessionLimit, long readSessionLimit, long delayLimit) {
        long newWriteLimit = writeGlobalLimit > 1024 ? writeGlobalLimit : serverGlobalWriteLimit;
        if (writeGlobalLimit <= 0) {
            newWriteLimit = 0;
        }
        long newReadLimit = readGlobalLimit > 1024 ? readGlobalLimit : serverGlobalReadLimit;
        if (readGlobalLimit <= 0) {
            newReadLimit = 0;
        }
        serverGlobalReadLimit = newReadLimit;
        serverGlobalWriteLimit = newWriteLimit;
        this.delayLimit = delayLimit;
        if (globalTrafficShapingHandler != null) {
            globalTrafficShapingHandler.configure(serverGlobalWriteLimit, serverGlobalReadLimit, delayLimit);
            logger.warn("Bandwidth limits change: {}", globalTrafficShapingHandler);
        }
        newWriteLimit = writeSessionLimit > 1024 ? writeSessionLimit : serverChannelWriteLimit;
        if (writeSessionLimit <= 0) {
            newWriteLimit = 0;
        }
        newReadLimit = readSessionLimit > 1024 ? readSessionLimit : serverChannelReadLimit;
        if (readSessionLimit <= 0) {
            newReadLimit = 0;
        }
        serverChannelReadLimit = newReadLimit;
        serverChannelWriteLimit = newWriteLimit;
    }

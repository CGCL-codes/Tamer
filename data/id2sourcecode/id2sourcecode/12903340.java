    public void run() {
        try {
            for (; ; ) {
                fdtSelectionKey = null;
                FDTSelectionKey iFDTSelectionKey = null;
                while ((iFDTSelectionKey = readyChannelsQueue.poll(2, TimeUnit.SECONDS)) == null) {
                    if (isClosed()) {
                        break;
                    }
                }
                fdtSelectionKey = iFDTSelectionKey;
                if (isClosed()) break;
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, " writeDate for SK: " + Utils.toStringSelectionKey(fdtSelectionKey) + " SQSize : " + readyChannelsQueue.size() + " SelQueue: " + readyChannelsQueue);
                }
                if (writeData() < 0) {
                    return;
                }
            }
        } catch (Throwable t) {
            master.workerDown(fdtSelectionKey, t);
            close("SocketWriterTask got exception ", t);
        } finally {
            try {
                if (fdtSelectionKey != null) {
                    FDTKeyAttachement attach = fdtSelectionKey.attachment();
                    if (attach != null) {
                        attach.recycleBuffers();
                    }
                }
            } catch (Throwable t1) {
                logger.log(Level.WARNING, " Got exception trying to return buffers to the pool", t1);
            }
            master.workerDown(fdtSelectionKey, null);
            close(null, null);
        }
    }

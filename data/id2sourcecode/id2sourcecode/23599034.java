    public FileChannel getChannel() throws Exception {
        if (channelInitialized) {
            if (isClosed()) {
                if (!isLoop) {
                    throw new Exception("Stream closed!");
                }
            } else {
                return fileChannel;
            }
        }
        synchronized (closeLock) {
            if (channelInitialized) {
                if (isClosed()) {
                    throw new Exception("Stream closed!");
                }
                return fileChannel;
            }
            if (isClosed()) {
                throw new Exception("Stream closed!");
            }
            try {
                fileChannel = this.fileChannelProvider.getFileChannel(tmpCopyFile, openMode);
                if (!noLock && !isNull) {
                    try {
                        fLock = fileChannel.lock();
                        if (logger.isLoggable(Level.FINE)) {
                            if (fLock == null) {
                                logger.log(Level.FINE, "[ FileWriterSession ] Cannot lock file: " + tmpCopyFile + "; will try to write without lock taken. No reason given.");
                            } else {
                                logger.log(Level.FINE, "[ FileWriterSession ] File lock for: " + tmpCopyFile + " taken!");
                            }
                        }
                    } catch (Throwable t) {
                        fLock = null;
                        logger.log(Level.WARNING, "[ FileWriterSession ] Cannot lock file: " + tmpCopyFile + "; will try to write without lock taken. Cause:", t);
                    }
                } else {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, "[ FileWriterSession ] Not using file lock for: " + tmpCopyFile);
                    }
                }
                channelInitialized = true;
            } catch (Exception ex) {
                close(null, ex);
                throw ex;
            }
        }
        return fileChannel;
    }

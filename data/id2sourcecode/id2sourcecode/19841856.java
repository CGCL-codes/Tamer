    public static final void copyFile2File(File s, File d, boolean noLock) throws IOException {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new RandomAccessFile(s, "rw").getChannel();
            dstChannel = new FileOutputStream(d).getChannel();
            try {
                if (!noLock) {
                    srcChannel.lock();
                } else {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, " [ Utils ] [ copyFile2File ] not taking locks for: " + s);
                    }
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Unable to take source file (" + s + ") lock. Will continue without lock taken. Cause: ", t);
            }
            try {
                if (!noLock) {
                    dstChannel.lock();
                } else {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE, " [ Utils ] [ copyFile2File ] not taking locks for: " + d);
                    }
                }
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Unable to take destination file (" + d + ") lock. Will continue without lock taken. Cause: ", t);
            }
            final long tr = dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            long ss = srcChannel.size();
            long ds = dstChannel.size();
            if (ss != ds || ss != tr) {
                throw new IOException("Different size for sourceFile [ " + s + " ] DestinationFileSize [ " + d + " ] Transferred [ " + tr + " ] ");
            }
        } finally {
            closeIgnoringExceptions(srcChannel);
            closeIgnoringExceptions(dstChannel);
        }
    }

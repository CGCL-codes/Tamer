    public ReadableByteChannel getDirectReadableChannel() {
        try {
            if (!file.exists()) {
                throw new IOException("File does not exist: " + file);
            }
            ReadableByteChannel channel = null;
            if (allowRandomAccess) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                channel = randomAccessFile.getChannel();
            } else {
                InputStream is = new FileInputStream(file);
                channel = Channels.newChannel(is);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Opened write channel to file: \n" + "   file: " + file + "\n" + "   random-access: " + allowRandomAccess);
            }
            return channel;
        } catch (Throwable e) {
            throw new ContentIOException("Failed to open file channel: " + this, e);
        }
    }

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
            return channel;
        } catch (Throwable e) {
            throw new ContentException("Failed to open file channel: " + this);
        }
    }

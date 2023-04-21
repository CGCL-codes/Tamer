    public FileChannel getChannel() throws Exception {
        synchronized (closeLock) {
            if (isClosed()) {
                if (!isLoop) {
                    throw new IOException("FileReaderSession closed!");
                }
            } else {
                if (this.fileChannel != null) {
                    return this.fileChannel;
                }
            }
            try {
                fileChannel = this.fileChannelProvider.getFileChannel(file, null);
            } catch (Exception ex) {
                close("Cannot instantiate fileChannel", ex);
                throw ex;
            }
        }
        return fileChannel;
    }

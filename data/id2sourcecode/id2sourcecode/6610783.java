    public long write() throws IOException {
        if (!fileChannel.isOpen()) {
            throw new IllegalStateException("Already written, can write only once");
        }
        final long bytesWritten = writeCrap();
        close();
        return bytesWritten;
    }

    @Override
    public synchronized ReadableByteChannel getChannel() throws IOException {
        ReadableByteChannel result = this.readableChannel;
        this.readableChannel = null;
        setAvailable(false);
        return result;
    }

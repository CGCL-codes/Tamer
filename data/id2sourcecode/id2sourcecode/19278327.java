    public long transferFrom(ReadableByteChannel source, int chunkSize) throws IOException, BufferOverflowException {
        synchronized (delegate) {
            return delegate.transferFrom(source, chunkSize);
        }
    }

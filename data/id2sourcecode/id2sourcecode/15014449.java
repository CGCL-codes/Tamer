    public final long transferFrom(ReadableByteChannel source, int chunkSize) throws IOException {
        return delegate.transferFrom(source, chunkSize);
    }

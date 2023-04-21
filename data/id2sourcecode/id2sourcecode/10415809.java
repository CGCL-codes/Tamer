    public long transferFrom(ReadableByteChannel source) throws IOException, BufferOverflowException {
        synchronized (delegate) {
            return delegate.transferFrom(source);
        }
    }

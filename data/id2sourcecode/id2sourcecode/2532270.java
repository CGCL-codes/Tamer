    @Override
    public long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
        synchronized (this.lock) {
            return this.delegate.transferFromByteChannel(in, maxCount);
        }
    }

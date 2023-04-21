    @Override
    public long transferFrom(final InputStream in, final long maxCount) throws IOException {
        synchronized (this.lock) {
            return this.delegate.transferFrom(in, maxCount);
        }
    }

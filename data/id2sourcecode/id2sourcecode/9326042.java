    @Override
    public long transferFrom(Readable in, long maxCount) throws IOException {
        synchronized (this.lock) {
            return this.delegate.transferFrom(in, maxCount);
        }
    }

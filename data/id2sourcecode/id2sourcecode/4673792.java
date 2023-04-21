    @Override
    public long transferFrom(Readable in, long maxCount) throws IOException {
        ensureOpen();
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        if (in instanceof Reader) return transferFromImpl((Reader) in, maxCount); else return super.transferFrom(in, maxCount);
    }

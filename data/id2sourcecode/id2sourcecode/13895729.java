    @Override
    public long transferFrom(final InputStream in, long maxCount) throws IOException {
        if (maxCount < 0) maxCount = this.remaining; else maxCount = Math.min(maxCount, this.remaining);
        final OutputStream out = checkWrite(maxCount);
        if (maxCount == 0) return 0;
        final long count = IOStreams.transfer(in, out, maxCount);
        if (count > 0) written(count);
        return count;
    }

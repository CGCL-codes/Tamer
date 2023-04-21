    @Override
    public long transferFromByteChannel(final ReadableByteChannel in, long maxCount) throws IOException {
        if (maxCount < 0) maxCount = this.remaining; else maxCount = Math.min(maxCount, this.remaining);
        final OutputStream out = checkWrite(maxCount);
        if (maxCount == 0) return 0;
        final long count;
        if (out instanceof OutputByteChannel) {
            count = ((OutputByteChannel) out).transferFromByteChannel(in, maxCount);
            written(count);
        } else {
            count = super.transferFromByteChannel(in, maxCount);
        }
        return count;
    }

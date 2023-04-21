    @Override
    public long transferFromByteChannel(final ReadableByteChannel in, final long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        final ByteBuffer buf = prepareWrite();
        if (buf == null) return 0;
        int step = (maxCount < 0) ? buf.remaining() : (int) Math.min(buf.remaining(), maxCount);
        final int lim = buf.limit();
        try {
            buf.limit(buf.position() + step);
            step = Math.max(0, in.read(buf));
        } finally {
            buf.limit(lim);
        }
        if (!buf.hasRemaining()) handshake(wrap());
        return step;
    }

    @Override
    public long transferFrom(final Readable in, final long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        long count = 0;
        final CharBuffer cb = inBuffer();
        try {
            while (true) {
                if (!cb.hasRemaining()) {
                    cb.flip();
                    encode(cb, false);
                    cb.compact();
                }
                final int maxStep = (maxCount < 0) ? cb.remaining() : (int) Math.min(cb.remaining(), maxCount - count);
                cb.limit(cb.position() + maxStep);
                final int step = in.read(cb);
                if (step < 0) break; else if (step <= maxStep) count += step; else throw new ReturnValueException(in, "read(CharBuffer)", step, "<=", maxStep);
                if ((maxCount > 0) && (count >= maxCount)) break;
            }
        } finally {
            cb.limit(cb.capacity());
        }
        return count;
    }

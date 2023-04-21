    @Override
    public long transferFrom(Readable in, long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        long count = 0;
        while ((count < maxCount) || (maxCount < 0)) {
            CharBuffer buffer = ensureOpen(1);
            int maxStep = Math.min(buffer.remaining(), (int) Math.max(0, maxCount - count));
            final int lim = buffer.limit();
            buffer.limit(buffer.position() + maxStep);
            try {
                int step = in.read(buffer);
                if (step < 0) break; else if (step <= maxStep) count += step; else throw new ReturnValueException(in, "read(CharBuffer)", step, "<=", maxStep);
            } finally {
                buffer.limit(lim);
            }
        }
        return count;
    }

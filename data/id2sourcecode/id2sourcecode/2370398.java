    @Override
    public long transferFrom(Readable in, long maxCount) throws IOException {
        ensureOpen();
        CharBuffer buf = this.buffer;
        CheckArg.maxCount(maxCount);
        long count = 0;
        while (true) {
            if (count == maxCount) break;
            int maxStep = buf.remaining();
            if (maxStep == 0) {
                buf = prepareBuffer((maxCount) < 0 ? 1 : (int) Math.min(buf.capacity() + 1, maxCount - count));
                maxStep = (maxCount < 0) ? buf.remaining() : (int) Math.min(buf.remaining(), maxCount - count);
            }
            int pos = buf.position();
            int lim = buf.limit();
            buf.limit(buf.position() + maxStep);
            int step;
            try {
                step = in.read(buf);
            } finally {
                buf.limit(lim);
            }
            if (step < 0) break; else if (step > maxStep) throw new ReturnValueException(in, "read(CharBuffer)", step, "<=", maxStep); else if (buf.position() != pos + step) throw new ExternalAssertionException(in, "read(CharBuffer)");
            count += step;
        }
        return count;
    }

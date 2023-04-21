    @Override
    public long transferFrom(final InputStream in, final long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        long count = 0;
        while ((count < maxCount) || (maxCount < 0)) {
            final ByteBuffer buffer = ensureOpen((maxCount < 0) ? 8192 : (int) Math.min(8192, maxCount));
            final int maxStep = Math.min(buffer.remaining(), (int) Math.max(0, maxCount - count));
            final int step = in.read(buffer.array(), buffer.position(), maxStep);
            if (step < 0) {
                break;
            } else if (step <= maxStep) {
                count += step;
                buffer.position(buffer.position() + step);
            } else {
                throw new ReturnValueException(in, "read(byte[],int,int)", step, "<=", maxStep);
            }
        }
        return count;
    }

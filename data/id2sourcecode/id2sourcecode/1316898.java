    public static long transferFromByteChannel(final ReadableByteChannel src, final FileChannel dest, final long maxCount) throws IOException {
        CheckArg.maxCount(maxCount);
        if (maxCount == 0) return 0;
        if (src == dest) {
            final long pos = dest.position();
            final long count = (maxCount < 0) ? (dest.size() - pos) : Math.min(dest.size() - pos, maxCount);
            dest.position(pos + count);
            return count;
        }
        long count = 0;
        long pos = dest.position();
        ByteBuffer testBuffer = null;
        while ((count < maxCount) || (maxCount < 0)) {
            final long step = dest.transferFrom(src, pos, (maxCount < 0) ? Integer.MAX_VALUE : (maxCount - count));
            count += step;
            if (step > 0) {
                pos += step;
                dest.position(pos);
            }
            if (step <= 0) {
                if (testBuffer == null) testBuffer = ByteBuffer.allocate((maxCount < 0) ? 32 : (int) Math.min(32, maxCount - count)); else testBuffer.clear();
                final int testRead = src.read(testBuffer);
                if (testRead < 0) break;
                if (testRead > 0) {
                    testBuffer.flip();
                    dest.write(testBuffer);
                    count += testRead;
                    pos += testRead;
                    dest.position(pos);
                }
            }
        }
        return count;
    }

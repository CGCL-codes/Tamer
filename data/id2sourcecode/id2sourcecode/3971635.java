    int fillWithPartOfRange(ByteBuffer dst, Range sourceRange, long overlapBytes, int maxCopyLength) throws IOException {
        int dstInitialPosition = dst.position();
        if (sourceRange.data instanceof ByteBuffer) {
            ByteBuffer src = (ByteBuffer) sourceRange.data;
            src.limit((int) (sourceRange.dataOffset + sourceRange.length));
            src.position((int) (sourceRange.dataOffset + overlapBytes));
            if (src.remaining() > dst.remaining() || src.remaining() > maxCopyLength) {
                src.limit(src.position() + Math.min(dst.remaining(), maxCopyLength));
            }
            dst.put(src);
        } else if (sourceRange.data instanceof RandomAccessFile) {
            RandomAccessFile src = (RandomAccessFile) sourceRange.data;
            long start = sourceRange.dataOffset + overlapBytes;
            int length = (int) Math.min(sourceRange.length - overlapBytes, maxCopyLength);
            int limit = -1;
            if (dst.remaining() > length) {
                limit = dst.limit();
                dst.limit(dst.position() + length);
            }
            src.getChannel().read(dst, start);
            if (limit > 0) dst.limit(limit);
        }
        return dst.position() - dstInitialPosition;
    }

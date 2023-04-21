    private static int writeImpl(final FileChannel dest, final Buffer src, final ByteOrder order, final JavaTypeId type, final int shift) throws IOException {
        final int count = src.remaining();
        if (count == 0) return 0;
        final ReadableByteChannel srcChannel = new FileChannels.BufferConverterChannel(src, order, type);
        long pos = dest.position();
        while (src.hasRemaining()) pos += dest.transferFrom(srcChannel, pos, ((long) src.remaining()) << shift);
        dest.position(pos);
        return count;
    }

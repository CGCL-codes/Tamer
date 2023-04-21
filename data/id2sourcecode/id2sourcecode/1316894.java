    public static void transferBytesFullyFrom(final DataInput src, final FileChannel dest, long count) throws IOException {
        CheckArg.count(count);
        if (count == 0) return;
        if (src == dest) {
            final long fp = dest.position();
            final long size = dest.size();
            dest.position(Math.min(fp + count, size));
            if (fp + count > size) throw new EOFException();
            return;
        }
        final ReadableByteChannel srcChannel;
        if (src instanceof ReadableByteChannel) srcChannel = (ReadableByteChannel) src; else if (src instanceof RandomAccessFile) srcChannel = ((RandomAccessFile) src).getChannel(); else if (src == null) throw new NullPointerException("source"); else {
            srcChannel = new ReadableByteChannel() {

                @Override
                public final void close() {
                }

                @Override
                public final boolean isOpen() {
                    return true;
                }

                @Override
                public final int read(final ByteBuffer dest) throws IOException {
                    final int count = dest.remaining();
                    if (dest.hasArray()) {
                        src.readFully(dest.array(), dest.arrayOffset() + dest.position(), count);
                        dest.position(dest.limit());
                    } else {
                        for (int r = count; --r >= 0; ) dest.put(src.readByte());
                    }
                    return count;
                }
            };
        }
        if (transferFromByteChannel(srcChannel, dest, count) != count) {
            throw new EOFException();
        }
    }

    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        if (position < 0 || count < 0) throw new IllegalArgumentException("position: " + position + ", count: " + count);
        if (!isOpen()) throw new ClosedChannelException();
        if ((mode & WRITE) == 0) throw new NonWritableChannelException();
        final int pageSize = 65536;
        long total = 0;
        while (count > 0) {
            int transferred = smallTransferFrom(src, position, (int) Math.min(count, pageSize));
            if (transferred < 0) break;
            total += transferred;
            position += transferred;
            count -= transferred;
        }
        return total;
    }

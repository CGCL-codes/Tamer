    private long tranferFromFileChannelWorkAround(FileChannel src, long position, long count) throws IOException {
        final long remaining = src.size() - src.position();
        if (remaining == 0) return 0;
        if (count > remaining) count = remaining;
        return inner.transferFrom(src, position, count);
    }

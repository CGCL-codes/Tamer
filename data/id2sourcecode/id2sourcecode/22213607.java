    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        final long FS_LIMIT = 0x1000000;
        if (count > FS_LIMIT) count = FS_LIMIT;
        if (src instanceof FileChannel) return tranferFromFileChannelWorkAround((FileChannel) src, position, count); else return inner.transferFrom(src, position, count);
    }

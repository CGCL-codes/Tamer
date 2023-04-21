    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        ensureOpen();
        if (!src.isOpen()) throw new ClosedChannelException();
        if (!writable) throw new NonWritableChannelException();
        if ((position < 0) || (count < 0)) throw new IllegalArgumentException();
        if (position > size()) return 0;
        if (src instanceof FileChannelImpl) return transferFromFileChannel((FileChannelImpl) src, position, count);
        return transferFromArbitraryChannel(src, position, count);
    }

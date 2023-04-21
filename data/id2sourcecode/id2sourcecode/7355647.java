    @Override
    public long transferFrom(final ReadableByteChannel src, final long position, final long count) throws IOException {
        return delegate().transferFrom(src, position, count);
    }

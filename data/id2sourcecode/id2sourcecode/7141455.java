    public static void transferFullyFrom(final ReadableByteChannel src, final long position, final long count, final FileChannel dst) throws IOException {
        long pos = position;
        long remaining = count;
        while (remaining > 0) {
            long wrote = dst.transferFrom(src, pos, remaining);
            pos += wrote;
            remaining -= wrote;
        }
    }

    public static void fill(final FileChannel f, final byte e, final long count) throws IOException {
        if (count == 0) return;
        CheckArg.notNegative(count, "count");
        final class ByteCopiesChannel extends Object implements ReadableByteChannel {

            long remaining = count;

            ByteCopiesChannel() {
                super();
            }

            @Override
            public final boolean isOpen() {
                return true;
            }

            @Override
            public final void close() {
            }

            @Override
            public final int read(final ByteBuffer dest) {
                return ByteBuffers.fill(dest, e);
            }
        }
        transferFromByteChannel(new ByteCopiesChannel(), f, count);
    }

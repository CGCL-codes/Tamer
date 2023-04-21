    private long transferFromFileChannel(FileChannelImpl src, long position, long count) throws IOException {
        synchronized (src.positionLock) {
            long p = src.position();
            int icount = (int) Math.min(Math.min(count, Integer.MAX_VALUE), src.size() - p);
            MappedByteBuffer bb = src.map(MapMode.READ_ONLY, p, icount);
            try {
                long n = write(bb, position);
                src.position(p + n);
                return n;
            } finally {
                unmap(bb);
            }
        }
    }

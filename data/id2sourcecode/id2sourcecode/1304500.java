    public static MappedByteBuffer map(File file, long offset, long length, MapMode mode) throws IOException {
        String rafMode = mode.equals(MapMode.READ_ONLY) ? "r" : "rw";
        RandomAccessFile raf = new RandomAccessFile(file, rafMode);
        try {
            return raf.getChannel().map(mode, offset, length);
        } finally {
            IOUtil.closeQuietly(raf);
        }
    }

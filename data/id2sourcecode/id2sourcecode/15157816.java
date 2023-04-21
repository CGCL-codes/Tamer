    public ByteBuffer mapRW(final long position, final long size) throws FileNotFoundException, IOException {
        flush();
        final FileChannel fc = (channel != null) ? channel : new RandomAccessFile(name, "rw").getChannel();
        final ByteBuffer buf = fc.map(MapMode.READ_WRITE, position, size).order(ByteOrder.nativeOrder());
        fc.close();
        return buf;
    }

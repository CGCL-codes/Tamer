    public ByteBuffer mapR(final long position, final long size) throws FileNotFoundException, IOException {
        final FileChannel fc = (channel != null) ? channel : new FileInputStream(name).getChannel();
        final ByteBuffer buf = fc.map(MapMode.READ_ONLY, position, size).order(ByteOrder.nativeOrder());
        fc.close();
        return buf;
    }

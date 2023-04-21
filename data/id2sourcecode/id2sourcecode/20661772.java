    @Override
    public synchronized RandomAccessFile setReadOnly(RandomAccessFile raf, String path) throws IOException {
        MappedByteBuffer buf = bufMap.remove(raf);
        try {
            safeForce(buf);
        } finally {
            if (!clean(buf)) {
                buf = null;
                System.gc();
            }
        }
        raf = super.setReadOnly(raf, path);
        buf = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
        bufMap.put(raf, buf);
        return raf;
    }

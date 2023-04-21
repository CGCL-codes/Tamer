    public ByteReader(File f) throws IOException {
        super();
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        FileChannel fc = raf.getChannel();
        if (fc.size() > Integer.MAX_VALUE) throw new IOException("File size too large to handle in memory maps");
        backBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size());
        backBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

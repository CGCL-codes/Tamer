    public BLPFile(File f) throws IOException, BLPFileException {
        FileChannel fc = new FileInputStream(f).getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        read(bb);
    }

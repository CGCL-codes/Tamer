    public SkinFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        read(bb);
    }

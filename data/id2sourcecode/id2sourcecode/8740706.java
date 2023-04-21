    public void load(File file) throws IOException, M2FileException, URISyntaxException {
        FileChannel fc = new FileInputStream(file).getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        read(bb.order(ByteOrder.LITTLE_ENDIAN));
    }

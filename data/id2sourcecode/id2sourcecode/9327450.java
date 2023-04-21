    public ReadOnlyCDB(String filename) throws IOException {
        file = new RandomAccessFile(filename, "r");
        FileChannel channel = file.getChannel();
        map = channel.map(MapMode.READ_ONLY, 0, file.length());
        map.order(ByteOrder.LITTLE_ENDIAN);
    }

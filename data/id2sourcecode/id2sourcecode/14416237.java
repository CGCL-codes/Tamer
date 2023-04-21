    private static String getHash(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        long sz = fc.size();
        if (sz < 65536) {
            fc.close();
            return "NoHash";
        }
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, 65536);
        long sum = sz;
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 65536 / 8; i++) {
            sum += bb.getLong();
        }
        bb = fc.map(FileChannel.MapMode.READ_ONLY, sz - 65536, 65536);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < 65536 / 8; i++) {
            sum += bb.getLong();
        }
        sum = sum & 0xffffffffffffffffL;
        String s = String.format("%016x", sum);
        fc.close();
        return s;
    }

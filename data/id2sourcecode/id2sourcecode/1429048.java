    private static void grep(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        CharBuffer cb = decoder.decode(bb);
        grep(f, cb);
        fc.close();
    }

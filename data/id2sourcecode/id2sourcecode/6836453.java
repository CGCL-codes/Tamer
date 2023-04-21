    public static CDF getCDF(String fname) throws IOException {
        File file = new File(fname);
        FileInputStream fis = new FileInputStream(file);
        FileChannel ch = fis.getChannel();
        ByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
        return getVersion(buf);
    }

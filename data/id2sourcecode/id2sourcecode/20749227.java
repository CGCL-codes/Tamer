    public static byte[] readFile(File file) throws FileNotFoundException, IOException {
        requireFile(file);
        FileChannel source = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) source.size());
        source.read(buf);
        source.close();
        return buf.array();
    }

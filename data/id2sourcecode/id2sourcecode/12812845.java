    public static ByteBuffer readDataFile(String fileName) throws IOException {
        FileChannel fcData = new FileInputStream(fileName).getChannel();
        int len = (int) new File(fileName).length();
        ByteBuffer buf = ByteBuffer.allocateDirect(len);
        buf.rewind();
        int numRead = fcData.read(buf);
        buf.rewind();
        if (len != numRead) throw new IOException("Less bytes were read than expected: " + len + " - " + numRead);
        return buf;
    }

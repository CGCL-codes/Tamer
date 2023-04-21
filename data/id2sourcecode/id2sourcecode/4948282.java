    private static String readFile(File file) throws Exception {
        FileChannel chan = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) chan.size());
        chan.read(buf);
        chan.close();
        buf.rewind();
        return new String(Charset.forName("UTF-8").newDecoder().decode(buf).array());
    }

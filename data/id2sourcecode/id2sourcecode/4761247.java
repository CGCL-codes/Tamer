    public static CharSequence fromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bbuf = ByteBuffer.allocate((int) fc.size());
        fc.read(bbuf);
        bbuf.position(0);
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        return cbuf;
    }

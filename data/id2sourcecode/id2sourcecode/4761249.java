    public static void toFile(File file, StringBuffer sb) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fc = fos.getChannel();
        CharBuffer cb = CharBuffer.allocate(sb.length());
        cb.put(sb.toString());
        cb.position(0);
        ByteBuffer bbf = Charset.forName("8859_1").newEncoder().encode(cb);
        fc.write(bbf);
    }

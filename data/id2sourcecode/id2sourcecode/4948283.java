    private static void writeFile(File file, String data) throws Exception {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileChannel chan = new FileOutputStream(file).getChannel();
        CharBuffer cbuf = CharBuffer.allocate(data.length()).put(data);
        cbuf.rewind();
        ByteBuffer buf = Charset.forName("UTF-8").newEncoder().encode(cbuf);
        buf.rewind();
        chan.write(buf);
        chan.close();
    }

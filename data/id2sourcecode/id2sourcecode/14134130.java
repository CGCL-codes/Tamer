    private CharSequence toCharSequence(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
        CharBuffer cbuf = Charset.forName("UTF-8").newDecoder().decode(bbuf);
        return cbuf;
    }

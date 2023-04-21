    public static CharSequence charSequenceFromFile(String filename) throws Exception {
        if (!new File(filename).exists()) {
            throw new Exception("File not found " + filename);
        }
        FileInputStream fis = new FileInputStream(filename);
        FileChannel fc = fis.getChannel();
        ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
        CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
        fc.close();
        fis.close();
        bbuf.clear();
        bbuf = null;
        System.gc();
        return cbuf;
    }

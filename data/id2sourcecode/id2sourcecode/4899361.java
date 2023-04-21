    public MorkParser(Reader r) throws IOException {
        if (r == null) throw new IllegalArgumentException();
        CharArrayWriter caw = new CharArrayWriter();
        int c;
        while ((c = r.read()) != -1) caw.write(c);
        caw.close();
        data = caw.toCharArray();
        pos = 0;
        if (data == null || data.length == 0) throw new IOException("File is empty.");
    }

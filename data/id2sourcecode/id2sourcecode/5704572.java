    public static void copy(Reader src, Writer dest) throws IOException {
        char[] b = new char[BUFSIZE];
        int readChars;
        while ((readChars = src.read(b)) > 0) dest.write(b, 0, readChars);
    }

    public static void copy(InputStream src, OutputStream dest) throws IOException {
        byte[] b = new byte[BUFSIZE];
        int readBytes;
        while ((readBytes = src.read(b)) > 0) dest.write(b, 0, readBytes);
    }

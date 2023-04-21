    public static void copyStream(InputStream is, OutputStream out) throws IOException {
        int size = 1024;
        byte[] buf = new byte[size];
        int len;
        while ((len = is.read(buf, 0, size)) != -1) out.write(buf, 0, len);
    }

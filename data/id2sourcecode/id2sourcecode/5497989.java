    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        int count;
        byte[] buf = new byte[32768];
        while ((count = is.read(buf)) != -1) os.write(buf, 0, count);
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[2048];
        int read = 0;
        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }
    }

    public static void writeTo(final InputStream in, final OutputStream out) throws IOException {
        int read;
        final byte[] buf = new byte[2048];
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }
    }

    public static void copy(final OutputStream sink, final InputStream is) throws IOException {
        final byte[] b = new byte[8192];
        int read;
        while ((read = is.read(b)) != -1) {
            sink.write(b, 0, read);
        }
    }
